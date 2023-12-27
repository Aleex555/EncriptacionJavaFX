package com.project;

import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.jcajce.JcaPGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class EncriptadorGPG {

    public static void encriptarArchivo(File archivoEntrada, File archivoSalida, File clavePublicaFile)
            throws IOException, PGPException, NoSuchAlgorithmException {
        try (
                FileInputStream clavePublicaStream = new FileInputStream(clavePublicaFile);
                FileInputStream archivoEntradaStream = new FileInputStream(archivoEntrada);
                FileOutputStream archivoSalidaStream = new FileOutputStream(archivoSalida);) {
            PGPPublicKey clavePublica = cargarClavePublica(clavePublicaStream);
            PGPEncryptedDataGenerator encriptador = new PGPEncryptedDataGenerator(
                    new JcePGPDataEncryptorBuilder(PGPEncryptedDataGenerator.AES_256)
                            .setWithIntegrityPacket(true)
                            .setSecureRandom(new SecureRandom()));
            encriptador.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(clavePublica));

            int bufferSize = 4096;
            try (OutputStream outputStream = encriptador.open(archivoSalidaStream, new byte[bufferSize])) {
                comprimirYEncriptar(archivoEntradaStream, outputStream);
            }
        }
    }

    private static PGPPublicKey cargarClavePublica(InputStream in) throws IOException, PGPException {
        try (InputStream clavePublicaStream = PGPUtil.getDecoderStream(in)) {
            JcaPGPPublicKeyRingCollection pgpPublicKeyRingCollection = new JcaPGPPublicKeyRingCollection(
                    clavePublicaStream);
            return pgpPublicKeyRingCollection.getKeyRings().next().getPublicKeys().next();
        }
    }

    private static void comprimirYEncriptar(InputStream in, OutputStream out) throws IOException, PGPException {
        PGPCompressedDataGenerator comprimidoDataGenerator = new PGPCompressedDataGenerator(
                PGPCompressedDataGenerator.ZIP);
        try (OutputStream comprimidoOut = comprimidoDataGenerator.open(out)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                comprimidoOut.write(buffer, 0, bytesRead);
            }
        } finally {
            comprimidoDataGenerator.close();
        }
    }
}
