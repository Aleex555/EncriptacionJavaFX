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
        FileInputStream clavePublicaStream = new FileInputStream(clavePublicaFile);
        FileInputStream archivoEntradaStream = new FileInputStream(archivoEntrada);
        FileOutputStream archivoSalidaStream = new FileOutputStream(archivoSalida);

        PGPPublicKey clavePublica = cargarClavePublica(clavePublicaStream);

        // Configurar generador de datos cifrados
        PGPEncryptedDataGenerator encriptador = new PGPEncryptedDataGenerator(
                new JcePGPDataEncryptorBuilder(PGPEncryptedDataGenerator.AES_256)
                        .setWithIntegrityPacket(true)
                        .setSecureRandom(new SecureRandom())
                        .setProvider("BC"));
        encriptador.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(clavePublica).setProvider("BC"));

        // Comprimir y cifrar
        int bufferSize = 4096; // Tamaño del búfer, puedes ajustarlo según tus necesidades
        try (OutputStream outputStream = encriptador.open(archivoSalidaStream, new byte[bufferSize])) {
            comprimirYEncriptar(archivoEntradaStream, outputStream);
        }

        clavePublicaStream.close();
        archivoEntradaStream.close();
        archivoSalidaStream.close();
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
        try {
            try (OutputStream comprimidoOut = comprimidoDataGenerator.open(out)) {
                PGPUtil.writeFileToLiteralData(comprimidoOut, PGPLiteralData.BINARY, new File("filename.txt"));
            }
        } finally {
            comprimidoDataGenerator.close();
        }
    }
}
