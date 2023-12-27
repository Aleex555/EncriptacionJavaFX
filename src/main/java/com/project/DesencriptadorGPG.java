package com.project;

import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.jcajce.JcaPGPObjectFactory;
import org.bouncycastle.openpgp.jcajce.JcaPGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;

import java.io.*;
import java.security.NoSuchProviderException;

public class DesencriptadorGPG {

    public static void desencriptarArchivo(File archivoEntrada, File archivoSalida, File clavePrivadaFile,
            char[] passphrase)
            throws IOException, PGPException, NoSuchProviderException {
        try (
                FileInputStream clavePrivadaStream = new FileInputStream(clavePrivadaFile);
                FileInputStream archivoEntradaStream = new FileInputStream(archivoEntrada);
                FileOutputStream archivoSalidaStream = new FileOutputStream(archivoSalida);) {
            PGPObjectFactory pgpObjectFactory = new PGPObjectFactory(PGPUtil.getDecoderStream(archivoEntradaStream),
                    null);
            PGPEncryptedDataList encList = (PGPEncryptedDataList) pgpObjectFactory.nextObject();

            PGPPublicKeyEncryptedData encData = (PGPPublicKeyEncryptedData) encList.get(0);
            PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(
                    PGPUtil.getDecoderStream(clavePrivadaStream), new JcaKeyFingerprintCalculator());

            PGPSecretKey secretKey = pgpSec.getSecretKey(encData.getKeyID());
            InputStream clear = encData.getDataStream(
                    new JcaPGPDataDecryptorFactoryBuilder().setProvider("BC")
                            .build(secretKey.extractPrivateKey(passphrase)));

            pgpObjectFactory = new PGPObjectFactory(clear);
            Object message = pgpObjectFactory.nextObject();

            if (message instanceof PGPCompressedData) {
                PGPCompressedData compressedData = (PGPCompressedData) message;
                pgpObjectFactory = new PGPObjectFactory(compressedData.getDataStream());
                message = pgpObjectFactory.nextObject();
            }

            if (message instanceof PGPLiteralData) {
                PGPLiteralData literalData = (PGPLiteralData) message;
                try (InputStream unc = literalData.getInputStream();
                        OutputStream fOut = new BufferedOutputStream(new FileOutputStream(archivoSalida))) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = unc.read(buffer)) != -1) {
                        fOut.write(buffer, 0, bytesRead);
                    }
                }
            }
        }
    }
}
