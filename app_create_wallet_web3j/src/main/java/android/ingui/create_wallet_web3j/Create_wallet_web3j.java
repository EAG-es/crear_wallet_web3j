package android.ingui.create_wallet_web3j;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.security.Provider;
import java.security.Security;

public class Create_wallet_web3j extends AppCompatActivity {
    public static final int ID_REQUEST_PERMISSIONS_CODE_WRITE_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);
        try {
//            getWriteStoragePermission();
            if (setupBouncyCastle() == false) {
                throw new RuntimeException("Error on setupBouncyCastle() ");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            create_wallet("12345", "/android.ingui.create_wallet_web3j/assets/re");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean setupBouncyCastle() {
        final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if (provider == null) {
            // Web3j will set up the provider lazily when it's first used.
            return false;
        }
        if (provider.getClass().equals(org.bouncycastle.jce.provider.BouncyCastleProvider.class)) {
            return false;
        }
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        org.bouncycastle.jce.provider.BouncyCastleProvider bouncyCastleProvider = new org.bouncycastle.jce.provider.BouncyCastleProvider();
//            org.spongycastle.jce.provider.BouncyCastleProvider bouncyCastleProvider = new org.spongycastle.jce.provider.BouncyCastleProvider();
        Security.insertProviderAt(bouncyCastleProvider, 1);
        return true;
    }
    public File create_wallet(String password, String path) throws Exception {
        File result = null;
        File extern_dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = extern_dir;
        String [] dirs_array = path.split(File.separator);
        for (String dir: dirs_array) {
            file = new File(file, dir);
            if (file.exists() == false) {
                if (file.mkdir() == false) {
                    return null;
                }
            }
        }
        if (file.exists()) {
            if (file.isDirectory()) {
                String nombre_de_archivo = WalletUtils.generateLightNewWalletFile(password, file);
                result = new File(file, nombre_de_archivo);
            } else {
                return null;
            }
        } else {
            return null;
        }
        return result;
    }
//    private Boolean getWriteStoragePermission() {
//        if (ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            String [] strings_array = {WRITE_EXTERNAL_STORAGE};
//            requestPermissions(strings_array, ID_REQUEST_PERMISSIONS_CODE_WRITE_STORAGE);
//        }
//        return true;
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                           int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        boolean is_granted = false;
//        switch (requestCode) {
//        case ID_REQUEST_PERMISSIONS_CODE_WRITE_STORAGE:
//            if (grantResults.length > 0) {
//                is_granted = true;
//                for (int result: grantResults) {
//                    if (result != PackageManager.PERMISSION_GRANTED) {
//                        is_granted = false;
//                        break;
//                    }
//                }
//            }
//            if (is_granted == false) {
//                throw new RuntimeException("Permission not Granted: " + "WRITE_EXTERNAL_STORAGE");
//            }
//        }
//    }
}
