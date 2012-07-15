package nz.org.nesi.login;

import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.model.FileManager;
import grisu.model.MountPoint;

public class SshKeyCopy {

	/**
	 * @param args
	 */
	public static void main(String[] arg) throws Exception {

		ServiceInterface si = LoginManager.loginCommandline("nesi");

		FileManager fm = new FileManager(si);

		for (MountPoint mp : si.df().getMountpoints()) {

			System.out.println(mp.getRootUrl());

		}

	}

}
