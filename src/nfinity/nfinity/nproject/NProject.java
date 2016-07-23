package nfinity.nfinity.nproject;

import nfinity.nfinity.Nterpreter;
import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ncontext.NContextDiver;
import nfinity.nfinity.nassembly.AssemblySettings;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Dictionary;
import java.util.List;

/**
 * Insert description here
 *
 * @author Comic
 * @since 22/05/2016 2016
 */
public class NProject {
    public Dictionary<String, String> Defines;

    public String ProjectPath;

    public List<Path> Files;

    public NAssembly Assembly;

    public NContextDiver ContextDiver = null;

    public void addInclude(String pathstring) {
        Path foundPath = Paths.get(Defines.get("FILE_DIR"), pathstring.substring(1, pathstring.length() - 2));

        Files.add(Nterpreter.convertFile(foundPath));
    }
}
