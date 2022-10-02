package com.zerogift.support.utils;

import com.zerogift.product.infrastructure.s3.FileUtil;
import java.awt.image.BufferedImage;
import lombok.RequiredArgsConstructor;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import okhttp3.HttpUrl;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BarcodeUtils {
    private final FileUtil fileUtil;

    private static final String URL = "https://zerogift.p-e.kr/giftBox/giftcon";

    public String barcodeSave(Long giftBoxId, String code) {
        try {
            Barcode barcode = BarcodeFactory.createCode128B(makeUrl(giftBoxId, code));
            barcode.setDrawingText(false);
            barcode.setBarHeight(100);

            BufferedImage bufferedImage = BarcodeImageHandler.getImage(barcode);
            return fileUtil.update(bufferedImage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("파일 저장중 에러가 발생하였습니다.");
        }
    }

    private String makeUrl(Long giftBoxId, String code) {
        HttpUrl.Builder url = HttpUrl.parse(URL).newBuilder();
        url.addPathSegment(giftBoxId+"")
            .addQueryParameter("code", code);

        return url.toString();
    }

}
