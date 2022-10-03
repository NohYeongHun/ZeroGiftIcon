package com.zerogift.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.zerogift.acceptance.AcceptanceTest;
import com.zerogift.global.error.code.MemberErrorCode;
import com.zerogift.global.error.code.ProductErrorCode;
import com.zerogift.global.error.exception.MemberException;
import com.zerogift.global.error.exception.ProductException;
import com.zerogift.member.domain.AuthType;
import com.zerogift.member.domain.Member;
import com.zerogift.member.domain.MemberStatus;
import com.zerogift.member.domain.Role;
import com.zerogift.member.repository.MemberRepository;
import com.zerogift.product.application.ProductService;
import com.zerogift.product.application.dto.NewProductRequest;
import com.zerogift.product.application.dto.ProductDetailDto;
import com.zerogift.product.application.dto.ProductDto;
import com.zerogift.product.domain.Category;
import com.zerogift.product.domain.Product;
import com.zerogift.product.domain.Status;
import com.zerogift.product.repository.ProductRepository;
import com.zerogift.support.auth.userdetails.AdminInfo;
import com.zerogift.support.auth.userdetails.MemberInfo;
import com.zerogift.support.dto.Result;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class ProductServiceTest extends AcceptanceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    private static Member adminOne, adminTwo, memberThree, memberFour;
    private static Product productOne, productTwo, privateProductOne, soldOutProductOne;

    @BeforeAll
    void init() {
        adminOne = memberRepository.save(createMember("adminOne", Role.ROLE_ADMIN));
        adminTwo = memberRepository.save(createMember("adminTwo", Role.ROLE_ADMIN));
        memberThree = memberRepository.save(createMember("memberThree", Role.ROLE_MEMBER));
        memberFour = memberRepository.save(createMember("memberFour", Role.ROLE_MEMBER));
        productOne = productRepository.save(createProduct(adminOne, Status.PUBLIC));
        productTwo = productRepository.save(createProduct(adminTwo, Status.PUBLIC));
        privateProductOne = productRepository.save(createProduct(adminOne, Status.PRIVATE));
        soldOutProductOne = productRepository.save(createProduct(adminOne, Status.SOLDOUT));
    }

    @Test
    void listProductFilteringOutPrivateAndSoldOutProductTest() {
        setSignedInMember(adminOne);
        ResponseEntity<Result<?>> response = productService.listProduct(
            Arrays.asList(Category.FOOD, Category.BIRTHDAY), 0, 10);
        assertEquals(200, Objects.requireNonNull(response.getBody()).getStatus());
        List<ProductDto> data = (List<ProductDto>) response.getBody().getData();
        assertEquals(2, data.size());
    }

    @Test
    void listProductCategoryFilterTest() {
        setSignedInMember(adminOne);
        ResponseEntity<Result<?>> response = productService.listProduct(
            Arrays.asList(Category.COSMETIC, Category.BIRTHDAY), 0, 10);
        assertEquals(0, ((List<ProductDto>) response.getBody().getData()).size());
    }

    @Test
    void listProductAllowingRegularMemberAccessTest() {
        setSignedInMember(memberThree);
        ResponseEntity<Result<?>> response = productService.listProduct(
            List.of(Category.FOOD), 0, 10);
        assertEquals(2, ((List<ProductDto>) response.getBody().getData()).size());
    }

    @Test
    void listProductRejectingNonMemberTest() {
        Throwable exception = assertThrows(MemberException.class, () ->
            productService.listProduct(List.of(Category.FOOD), 0, 10));
        assertEquals(MemberErrorCode.MEMBER_NOT_FOUND.getDescription(), exception.getMessage());
    }

    @Test
    void searchProductFilteringOutPrivateAndSoldOutProductTest() {
        setSignedInMember(adminOne);
        ResponseEntity<Result<?>> response = productService.searchProduct(
            adminOne.getNickname(), 0, 10);
        assertEquals(1, ((List<ProductDto>) response.getBody().getData()).size());
    }

    @Test
    void searchProductPartialStringTest() {
        setSignedInMember(adminOne);
        ResponseEntity<Result<?>> response = productService.searchProduct(
            "est", 0, 10);
        assertEquals(2, ((List<ProductDto>) response.getBody().getData()).size());
    }

    @Test
    void searchProductRejectingNonMemberTest() {
        Throwable exception = assertThrows(MemberException.class, () ->
            productService.searchProduct(adminOne.getNickname(), 0, 10));
        assertEquals(MemberErrorCode.MEMBER_NOT_FOUND.getDescription(), exception.getMessage());
    }

    @Test
    void removeProductByOtherAdminTest() {
        AdminInfo adminSignedIn = buildSignedInAdmin(adminTwo);
        Throwable exception = assertThrows(ProductException.class, () ->
            productService.removeProduct(productOne.getId(), adminSignedIn));
        assertEquals(ProductErrorCode.OWNED_BY_SOMEONE_ELSE.getDescription(), exception.getMessage());
    }

    @Test
    void removeProductByRegularMemberTest() {
        MemberInfo memberSignedIn = buildSignedInMember(memberThree);
        Throwable exception = assertThrows(ProductException.class, () ->
            productService.removeProduct(productOne.getId(), memberSignedIn));
        assertEquals(ProductErrorCode.INSUFFICIENT_AUTHORITY.getDescription(), exception.getMessage());
    }

    @Test
    void addProductAndThenRemoveProductTest() {
        setSignedInMember(adminOne);
        ResponseEntity<Result<?>> response = productService.listProduct(
            Arrays.asList(Category.FOOD, Category.OTHER), 0, 10);
        List<ProductDto> data = (List<ProductDto>) response.getBody().getData();
        assertEquals(2, data.size());
        String description = "test product owned by adminOne which will be deleted";
        NewProductRequest request = NewProductRequest.builder()
            .name("test product")
            .description(description)
            .price(555)
            .count(100)
            .category(Category.OTHER)
            .productImageIds(new ArrayList<>())
            .build();
        productService.addProduct(request, buildSignedInAdmin(adminOne));

        response = productService.listProduct(
            Arrays.asList(Category.FOOD, Category.OTHER), 0, 10);
        data = (List<ProductDto>) response.getBody().getData();
        Long latestId = data.get(0).getId();
        assertNotEquals(productOne.getId(), latestId);
        assertNotEquals(productTwo.getId(), latestId);
        assertNotEquals(privateProductOne.getId(), latestId);
        assertNotEquals(soldOutProductOne.getId(), latestId);
        assertEquals(3, data.size());
        assertEquals(description, productRepository.findById(latestId).get().getDescription());

        productService.removeProduct(data.get(0).getId(), buildSignedInAdmin(adminOne));
        response = productService.listProduct(
            Arrays.asList(Category.FOOD, Category.OTHER), 0, 10);
        data = (List<ProductDto>) response.getBody().getData();
        assertEquals(2, data.size());
    }

    @Test
    void addProductRejectingNonAdminTest() {
        setSignedInMember(memberThree);
        ResponseEntity<Result<?>> response = productService.listProduct(
            Arrays.asList(Category.FOOD, Category.OTHER), 0, 10);
        List<ProductDto> data = (List<ProductDto>) response.getBody().getData();
        assertEquals(2, data.size());
        NewProductRequest request = NewProductRequest.builder()
            .name("test product")
            .description("test product owned by memberThree which will not be registered")
            .price(666)
            .count(100)
            .category(Category.OTHER)
            .productImageIds(new ArrayList<>())
            .build();
        Throwable exception = assertThrows(ProductException.class, () ->
            productService.addProduct(request, buildSignedInMember(memberThree)));
        assertEquals(ProductErrorCode.INSUFFICIENT_AUTHORITY.getDescription(), exception.getMessage());
    }

    @Test
    void getDetailNonExistingProductTest() {
        Throwable exception = assertThrows(ProductException.class, () ->
            productService.getDetail(5000L));
        assertEquals(ProductErrorCode.PRODUCT_NOT_FOUND.getDescription(), exception.getMessage());
    }

    @Test
    void getDetailTest() {
        ResponseEntity<Result<?>> response = productService.getDetail(productTwo.getId());
        ProductDetailDto detail = (ProductDetailDto) Objects.requireNonNull(response.getBody()).getData();
        assertEquals(productTwo.getId(), detail.getId());
        assertEquals(productTwo.getName(), detail.getName());
        assertEquals(productTwo.getDescription(), detail.getDescription());
        assertEquals(productTwo.getPrice(), detail.getPrice());
        assertEquals(productTwo.getCategory(), detail.getCategory());
    }

    @Test
    void getDetailPrivateProductRejectionTest() {
        setSignedInMember(memberFour);
        Throwable exception = assertThrows(ProductException.class, () ->
            productService.getDetail(privateProductOne.getId()));
        assertEquals(ProductErrorCode.PRIVATE_PRODUCT.getDescription(), exception.getMessage());
    }

    @Test
    void getDetailPrivateProductOwnerTest() {
        setSignedInMember(adminOne);
        ResponseEntity<Result<?>> response = productService.getDetail(privateProductOne.getId());
        ProductDetailDto detail = (ProductDetailDto) Objects.requireNonNull(response.getBody()).getData();
        assertEquals(privateProductOne.getId(), detail.getId());
        assertEquals(privateProductOne.getName(), detail.getName());
        assertEquals(privateProductOne.getDescription(), detail.getDescription());
        assertEquals(privateProductOne.getPrice(), detail.getPrice());
        assertEquals(privateProductOne.getCategory(), detail.getCategory());
    }

    @Test
    void getDetailSoldOutProductTest() {
        ResponseEntity<Result<?>> response = productService.getDetail(soldOutProductOne.getId());
        ProductDetailDto detail = (ProductDetailDto) Objects.requireNonNull(response.getBody()).getData();
        assertEquals(soldOutProductOne.getId(), detail.getId());
        assertEquals(soldOutProductOne.getName(), detail.getName());
        assertEquals(soldOutProductOne.getDescription(), detail.getDescription());
        assertEquals(soldOutProductOne.getPrice(), detail.getPrice());
        assertEquals(soldOutProductOne.getCategory(), detail.getCategory());
    }

    private static AdminInfo buildSignedInAdmin(Member member) {
        return new AdminInfo(member.getId(), member.getEmail(), member.getRole().toString());
    }

    private static MemberInfo buildSignedInMember(Member member) {
        return new MemberInfo(member.getId(), member.getRole().toString(), member.getNickname(),
            member.getStatus().toString(), member.getEmail());
    }

    private void setSignedInMember(Member member) {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(
            member.getRole().equals(Role.ROLE_ADMIN) ? buildSignedInAdmin(member)
                : buildSignedInMember(member));
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    private static Member createMember(String nickname, Role role) {
        return Member.builder()
            .authId(nickname)
            .authType(AuthType.KAKAO)
            .email(nickname + "@" + nickname + ".test")
            .nickname(nickname)
            .profileImageUrl(nickname + ".com/dummy")
            .status(MemberStatus.PERMITTED)
            .role(role)
            .build();
    }

    private static Product createProduct(Member member, Status status) {
        return Product.builder()
            .name("test product")
            .description("test product owned by " + member.getNickname())
            .price(100)
            .count(3)
            .category(Category.FOOD)
            .status(status)
            .member(member)
            .build();
    }
}
