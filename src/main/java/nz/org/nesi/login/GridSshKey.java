package nz.org.nesi.login;

import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.jcommons.configuration.CommonGridProperties;
import grisu.model.FileManager;
import grith.gridsession.GridClient;
import grith.jgrith.cred.Cred;

import java.io.File;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.KeyPair;

public class GridSshKey extends GridClient {

	public static final int KEY_TYPE = KeyPair.RSA;

	public static void createGridsshkey(char[] password, String id)
			throws Exception {

		if (gridsshkeyExists()) {
			throw new Exception("Key and/or Cert file(s) already exist.");
		}

		JSch jsch = new JSch();
		KeyPair kpair = KeyPair.genKeyPair(jsch, KEY_TYPE);
		kpair.setPassphrase(new String(password));
		kpair.writePrivateKey(CommonGridProperties.getDefault().getGridSSHKey());
		kpair.writePublicKey(
				CommonGridProperties.getDefault().getGridSSHCert(), id);
		kpair.dispose();
	}

	public static boolean gridsshkeyExists() {
		File key = new File(CommonGridProperties.getDefault().getGridSSHKey());
		if (!key.exists()) {
			return false;
		}
		File cert = new File(CommonGridProperties.getDefault().getGridSSHCert());
		if (!cert.exists()) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) throws Exception {

		try {
			if (!gridsshkeyExists()) {
				createGridsshkey("myPassword".toCharArray(), "testid");
			}

			GridSshKey key = new GridSshKey();

			Cred c = key.getCredential();

			ServiceInterface si = LoginManager.login("local", c, true);

			FileManager fm = new FileManager(si);
			// for (MountPoint mp : si.df().getMountpoints()) {
			//
			// System.out.println(mp.getRootUrl());
			//
			// }

			// GridFile f = fm.ls("gsiftp://pan.nesi.org.nz/~/");

			fm.uploadFile(new File(CommonGridProperties.getDefault()
					.getGridSSHCert().toString()),
					"gsiftp://pan.nesi.org.nz/~/.test/test", true);

		} catch (Exception e) {
			myLogger.debug("Upload of ssh cert failed.", e);
			System.exit(1);
		}
		System.exit(0);

	}

	public GridSshKey() throws Exception {
		super();
	}




}
