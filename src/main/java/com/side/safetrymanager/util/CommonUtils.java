package com.side.safetrymanager.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class CommonUtils {

    public static String createQR(String url, String fileDir) throws Exception {

        BitMatrix bitMatrix = null;
        MatrixToImageConfig matrixToImageConfig = null;
        // QRCode에 담고 싶은 정보를 문자열로 표시한다. url이든 뭐든 가능하다.
        String codeInformation = new String(url.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

        // 큐알코드 바코드 및 배경 색상값
        int qrcodeColor =   0xFF2e4e96; // 바코드 색
        int backgroundColor = 0xFFFFFFFF; // 배경 색

        // 이름 그대로 QRCode 만들때 쓰는 클래스다
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        // 큐알 전경과 배경의 색을 정한다. 값을 넣지 않으면 검정코드에 흰 배경이 기본값이다.
        matrixToImageConfig = new MatrixToImageConfig(qrcodeColor,backgroundColor);
        Map<EncodeHintType,String> hints = new HashMap<>();

        /*
        https://zxing.github.io/zxing/apidocs/com/google/zxing/qrcode/decoder/ErrorCorrectionLevel.html
        Enum Constants
        L = ~7% correction
        M = ~15% correction
        Q = ~25% correction
        H = ~30% correction
        */
        hints.put(EncodeHintType.ERROR_CORRECTION, "H");

        // QRCode 전체 크기
        // 단위는 fixel
        int width=400;
        int height=400;

        // 내부에 빈 공간만들 빈 공간 -> oncolor로 만들어진다.
//        int regionWidth=100;
//        int regionHeight=100;

        try {
            // bitMatrix 형식으로 QRCode를 만든다.
//            bitMatrix = qrCodeWriter.encode(codeInformation, BarcodeFormat.QR_CODE,width, height);

            // hints을 추가한 QRCode를 만든다.
            bitMatrix = qrCodeWriter.encode(codeInformation, BarcodeFormat.QR_CODE,width, height, hints);

            // QRCode 중간에 빈공간을 만들고 색을 offColor로 바꿔주는 메소드
//            bitMatrix= emptyQR(bitMatrix, regionHeight, regionWidth); // QR내부에 빈 공간 만드는 메소드(사용할 경우 hint의 error_correction 을 반드시 높여줘야 합니다)
        } catch (Exception e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream, matrixToImageConfig);

        // 이제 만들어본 QRCode를 저장해보자
        String savePath = fileDir;
        String qrName = "qrImage.png";
        File file = new File(savePath);
        if(!file.exists()) {
            file.mkdirs();
            // 리눅스 서버에 저장하는 경우 파일 접근 권한을 줘야 한다.
        }

        // bufferedImage 를 이용한 파일 저장 -> 방식 1
        BufferedImage bufferedImage = null;
        bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix,matrixToImageConfig);
        File saveFile=new File(savePath+qrName);
        ImageIO.write(bufferedImage, "png", saveFile);

        // fileOutputStream 을 이용한 파일 저장 -> 방식 2
//        FileOutputStream fileOutputStream=new FileOutputStream(new File(savePath+qrName));
//        fileOutputStream.write(outputStream.toByteArray());
//        fileOutputStream.close();


        // byteArray를 base64로 변환한 이유는 프론트에서 파일경로가 아닌 binary 형식으로 전송해서 보여주기 위해서다.
        // 이렇게 할 경우 DB에 이미지를 저장하지 않고 화면에 보여줄 수 있다.
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
}

    private static BitMatrix emptyQR(BitMatrix bitMatrix, int regionHeight, int regionWidth) {
        // 이 메소드는 bitmatrix에 네모난 공간을 만드는 것이다.

        // 빈 공간의 넓이와 높이
        int width=bitMatrix.getWidth();
        int height=bitMatrix.getHeight();

        // 빈 공간의 위치(중앙으로 설정했다.)
        int left=(width-regionWidth)/2;
        int top=(height-regionHeight)/2;

        // 빈 공간 생성하기(이때 색은 offColor)
        bitMatrix.setRegion(left,top,regionWidth,regionHeight);
        // 빈 공간의 색을 배경색으로 반전시킨다.(fixel 단위로 찾아서 색을 뒤집는다.)
        for (int y = top; y <= top+regionHeight; y++) {
            for (int x = left; x <= left+regionWidth; x++) {
                if(bitMatrix.get(x, y)){
                    bitMatrix.unset(x,y);
                };
            }
        }
        return bitMatrix;
    }
}
