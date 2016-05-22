package nfinity.nfinity.nproject;

import nfinity.nfinity.nassembly.NAssembly;

import java.nio.file.Path;
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
}
