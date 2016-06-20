package nfinity.nfinity;


import nfinity.nfinity.nproject.NProject;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {
	    NProject project = Nterpreter.openProject(Paths.get(args[0]));

        Nterpreter.assemble(project);
    }
}
