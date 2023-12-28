package com.project;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.PBESecretKeyDecryptor;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyDataDecryptorFactoryBuilder;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

public class DesencriptadorGPG {

    static {
     
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void desencriptarArchivo(File archivoEntrada, File archivoSalida, File clavePrivadaFile, String clavePrivadaPassphrase)
            throws IOException, PGPException, NoSuchAlgorithmException {
        try (
                FileInputStream clavePrivadaStream = new FileInputStream(clavePrivadaFile);
                FileInputStream archivoEntradaStream = new FileInputStream(archivoEntrada);
                FileOutputStream archivoSalidaStream = new FileOutputStream(archivoSalida)
        ) {
            PGPPrivateKey clavePrivada = cargarClavePrivada(clavePrivadaStream, clavePrivadaPassphrase);

            PGPObjectFactory objetoFactory = new PGPObjectFactory(PGPUtil.getDecoderStream(archivoEntradaStream),
                    new JcaKeyFingerprintCalculator());

            PGPEncryptedDataList datosEncriptados = (PGPEncryptedDataList) objetoFactory.nextObject();
            PGPPublicKeyEncryptedData datosEncriptadosSeleccionado = seleccionarDatosEncriptados(datosEncriptados);

            InputStream datosDesencriptadosStream = datosEncriptadosSeleccionado.getDataStream(
                    new JcePublicKeyDataDecryptorFactoryBuilder()
                            .setProvider("BC")
                            .build(clavePrivada));

            descomprimirYGuardar(datosDesencriptadosStream, archivoSalidaStream);
        }
    }

    private static PGPPublicKeyEncryptedData seleccionarDatosEncriptados(PGPEncryptedDataList datosEncriptados) {
        for (PGPEncryptedData datos : datosEncriptados) {
            if (datos instanceof PGPPublicKeyEncryptedData) {
                return (PGPPublicKeyEncryptedData) datos;
            }
        }
        return null;
    }

    private static PGPPrivateKey cargarClavePrivada(InputStream in, String passphrase)
            throws IOException, PGPException {
        try (InputStream clavePrivadaStream = PGPUtil.getDecoderStream(in)) {
            PGPSecretKeyRingCollection secretKeyRingCollection = new PGPSecretKeyRingCollection(
                    PGPUtil.getDecoderStream(clavePrivadaStream),
                    new JcaKeyFingerprintCalculator());

            List<PGPSecretKeyRing> secretKeyRings = new ArrayList<>();
            secretKeyRingCollection.getKeyRings().forEachRemaining(secretKeyRings::add);

            PGPSecretKey claveSecreta = secretKeyRings.get(0).getSecretKeys().next();

            PBESecretKeyDecryptor secretKeyDecryptor = new JcePBESecretKeyDecryptorBuilder()
                    .setProvider("BC")
                    .build(passphrase.toCharArray());

            return claveSecreta.extractPrivateKey(secretKeyDecryptor);
        }
    }

    private static void descomprimirYGuardar(InputStream in, OutputStream out) throws IOException, PGPException {
        PGPObjectFactory objetoFactory = new PGPObjectFactory(in, new JcaKeyFingerprintCalculator());
        Object objeto = objetoFactory.nextObject();
    
        if (objeto instanceof PGPCompressedData) {
            PGPCompressedData datosComprimidos = (PGPCompressedData) objeto;
            try (InputStream datosDescomprimidosStream = new BufferedInputStream(datosComprimidos.getDataStream());
                 OutputStream datosDescomprimidosOut = new BufferedOutputStream(out)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = datosDescomprimidosStream.read(buffer)) != -1) {
                    datosDescomprimidosOut.write(buffer, 0, bytesRead);
                }
            }
        } else if (objeto instanceof PGPLiteralData) {
            PGPLiteralData literalData = (PGPLiteralData) objeto;
            try (InputStream literalDataStream = literalData.getInputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = literalDataStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        } else {
            throw new PGPException("Tipo de dato PGP no compatible: " + objeto.getClass().getName());
        }
    }
    
}
