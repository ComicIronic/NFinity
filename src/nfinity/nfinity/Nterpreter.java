package nfinity.nfinity;

import nfinity.nfinity.exceptions.*;
import nfinity.nfinity.grammar.NFinityLexer;
import nfinity.nfinity.grammar.NFinityParse;
import nfinity.nfinity.ncontext.NAccess;
import nfinity.nfinity.ncontext.NContextDiver;
import nfinity.nfinity.ncontext.contexts.MethodContext;
import nfinity.nfinity.nmember.NField;
import nfinity.nfinity.nproject.NProject;
import nfinity.nfinity.nsignature.NArg;
import nfinity.nfinity.nsignature.NSignature;
import nfinity.nfinity.nsignature.request.NArgRequest;
import nfinity.nfinity.nsignature.request.NSigRequest;
import nfinity.nfinity.ntype.NType;
import nfinity.nfinity.ntype.core.Null;
import nfinity.nfinity.util.ListUtils;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Comic on 21/05/2016.
 */
public class Nterpreter {
    private static List<String> _messages = new ArrayList<String>();

    public static NProject CurrentProject = null;

    public static Path FilePath;
    public static int LineNumber;

    /**
     * Creates an NProject from the given DME Path, including creating new copies of all included code files if they don't already exist
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */
    public static NProject openProject(Path filePath) throws FileNotFoundException {
        CurrentProject = new NProject();

        processFile(filePath);

        for(String message : getMessages()) {
        	System.out.println(message);
        }

        return CurrentProject;
    }

    /**
     * Creates a converted version of a code file in the new format
     * @param filepath
     * @return
     */
    public static Path convertFile(Path filepath) {
        Path parent = filepath.getParent();

        Path convertedPath = Paths.get(parent.toString(), filepath.getFileName().toString().replace(".dm", ".idm"));

        if(!Files.exists(convertedPath)) {
            try {
                Files.copy(filepath, convertedPath);
            } catch (IOException e) {
            }
        }

        return convertedPath;
    }

    /**
     * Builds or rebuilds the given project
     * @param CurrentProject
     */
    public static void assemble(NProject CurrentProject) {
    	CurrentProject.Assembly.reset();

        CurrentProject.ContextDiver = new NContextDiver(CurrentProject.Assembly.WorldContext);
    	
    	for(Path filePath : CurrentProject.Files) {
            processFile(filePath);
    	}
    }

    /**
     * Opens the given file and processes it as code
     * @param filePath
     */
    private static void processFile(Path filePath) {
        try {
            ANTLRInputStream fileStream = new ANTLRFileStream(filePath.toString());

            NFinityLexer lexer = new NFinityLexer(fileStream);

            CommonTokenStream tokens = new CommonTokenStream(lexer);

            NFinityParse parser = new NFinityParse(tokens);

            while(!parser.isMatchedEOF()) {
                NFinityParse.LineContext line = parser.line();

                if(line != null) {
                    processLine(line);
                }
            }

        } catch (IOException e) {
            error(filePath, 0, "Could not read file.");
        }
    }

    /**
     * Processes a line as code
     * @param context
     */
    private static void processLine(NFinityParse.LineContext context) {
        NFinityParse.PreprocessContext preprocessor = context.preprocess();

        if(preprocessor != null) {
            if(preprocessor.DEFINE() != null) {
                CurrentProject.Defines.put(preprocessor.member_name().getText(), preprocessor.statement() != null? preprocessor.statement().getText() : "");
                return;
            }

            if(preprocessor.UNDEF() != null) {
                CurrentProject.Defines.remove(preprocessor.member_name().getText());
                return;
            }

            if(preprocessor.INCLUDE() != null) {
                CurrentProject.addInclude(preprocessor.bare_value().getText());
                return;
            }

            return;
        }

        NFinityParse.Type_blockContext typeContext = context.type_block();

        if(typeContext != null) {
            processType(typeContext);
        }

        return;
    }

    /**
     * Processes a type block into type information in the assembly
     * @param typeContext
     */
    private static void processType(NFinityParse.Type_blockContext typeContext) {
        String typepath = "";

        if(typeContext.type_declare() != null) {
            //There are multiple because we can be abstract
          for(NFinityParse.TypepathContext context : typeContext.type_declare().typepath()) {
              typepath += (typepath.length() > 0 ? "/" : "") + context.getText();
          }
        }

        if(typepath != "") {
            NType pathedType;
            try {
                pathedType = CurrentProject.ContextDiver.currentContext().createType(typepath);
            } catch (NTypeCannotExtendException e) {
                error(Nterpreter.FilePath, Nterpreter.LineNumber, "Attempted to extend an inextendable type: " + e.FinalType);
                return;
            }

            if(typeContext.type_declare().ABSTRACT() != null) {
                pathedType.Abstract = true;
            }

            //We dive into the type we've created
            CurrentProject.ContextDiver.diveInto(pathedType.TypeContext);
            //We process everything under it
            for(NFinityParse.Type_blockContext sub_block : typeContext.type_block()) {
                processType(sub_block);
            }
            //And then we pop back up
            CurrentProject.ContextDiver.riseUp();
            return;

        } else {
            NFinityParse.Method_declareContext methodDeclareContext = typeContext.method_declare();

            if (methodDeclareContext != null) {
                NAccess access = NAccess.Default;
                if (methodDeclareContext.access_modifier() != null) {
                    access = NAccess.valueOf(methodDeclareContext.access_modifier().getText());
                }



                try {
                    NSignature signature = new NSignature(
                            methodDeclareContext.member_name().getText(),
                            CurrentProject.Assembly.getTypeInPath(methodDeclareContext.typepath().getText()),
                            access,
                            processArgs(methodDeclareContext.argument_declares()));

                   CurrentProject.ContextDiver.currentContext().addMember(signature);
                } catch (NTypeNotFoundException e) {
                    error(Nterpreter.FilePath, Nterpreter.LineNumber, "Could not find the type " + e.FailedType);
                    return;
                } catch (NArgDeclareException e) {
                    error(Nterpreter.FilePath, Nterpreter.LineNumber, "Could not create arguments: " + e.Message);
                }
            }
        }
    }

    public static NArg[] processArgs(NFinityParse.Argument_declaresContext arguments) throws NArgDeclareException {
        List<NArg> args = new ArrayList<NArg>();

        boolean failure = false;
        
        for(NFinityParse.Argument_declareContext argument : arguments.argument_declare()) {


        	
            try {
                NSignature produced = processVarSig(argument.argument_var_declare().optional_var_declare());

                if(argument.statement() != null) {
                    NType defaultType = getReturnType(argument.statement());
                    if(produced.ReturnType.acceptsTypeAssign(defaultType)) {
                        args.add(new NArg(produced, true));
                    } else {
                        error(Nterpreter.FilePath, Nterpreter.LineNumber, produced.Name + " can't have a default value of type " + defaultType.typepath());
                        failure = true;
                    }
                } else {
                    args.add(new NArg(produced, false));
                }
            } catch (NTypeNotFoundException e) {
                error(Nterpreter.FilePath, Nterpreter.LineNumber, "Could not find type " + e.FailedType);
                failure = true;
            } catch (NMemberDeclareException e) {
            	error(Nterpreter.FilePath, Nterpreter.LineNumber, "Could not create variable " + e.MemberName + ": " + e.Message);
            	failure = true;
            } catch (BadNTypeException e) {
                error(Nterpreter.FilePath, Nterpreter.LineNumber, e.Message);
                failure = true;
            }
        }
        
        if(failure) {
        	throw new NArgDeclareException("Arguments were not defined properly");
        }

        return args.toArray(new NArg[args.size()]);
    }

    public static NSignature processVarSig(NFinityParse.Optional_var_declareContext context) throws NTypeNotFoundException, NMemberDeclareException {
        NAccess access = NAccess.Default;
        if(context.access_modifier() != null) {
            access = NAccess.valueOf(context.access_modifier().getText());
        }

        String fieldName = context.member_name().getText();
        NType fieldType = CurrentProject.Assembly.Any;

        if(context.typepath() != null) {
            fieldType = CurrentProject.Assembly.getTypeInPath(context.typepath().getText());
        }
        
        if(CurrentProject.Preferences.DisableAny && fieldType == CurrentProject.Assembly.Any) {
        	throw new NMemberDeclareException(fieldName, "Untyped variables are forbidden by preferences");
        }

        return new NSignature(fieldName, fieldType, access);
    }
    
    public static NType getReturnType(NFinityParse.StatementContext statement) throws BadNTypeException {
        return getReturnType(statement.trinary_statement());
    }

    public static NType getReturnType(NFinityParse.Trinary_statementContext trinary) throws BadNTypeException {
        if(trinary.statement() != null && trinary.statement().size() > 0) {
            NType ifType = getReturnType(trinary.statement(0));
            NType elseType = getReturnType(trinary.statement(1));

            NType shared = CurrentProject.Assembly.getDeepestShared(ifType, elseType);

            if(shared == CurrentProject.Assembly.Any) {
                if (CurrentProject.Preferences.DisableAny) {
                    throw new BadNTypeException("Untyped variables are forbidden by preferences");
                }
            }
            return shared;
        } else {
            return getReturnType(trinary.or_statement());
        }
    }

    public static NType getReturnType(NFinityParse.Or_statementContext or_statement) throws BadNTypeException {
        if(or_statement.and_statement().size() > 1) {
            if(CurrentProject.Preferences.RequireBoolean) {
                for(NFinityParse.And_statementContext and_statement : or_statement.and_statement()) {
                    if(getReturnType(and_statement) != CurrentProject.Assembly.Bool) {
                        throw new BadNTypeException("All conditional statements must be boolean");
                    }
                }
            }

            return CurrentProject.Assembly.Bool;
        } else {
            //Otherwise, our return type is just the sub-statement return type
            return getReturnType(or_statement.and_statement(0));
        }
    }

    public static NType getReturnType(NFinityParse.And_statementContext and_statement) throws BadNTypeException {
        if(and_statement.compare_statement().size() > 1) {
            if(CurrentProject.Preferences.RequireBoolean) {
                for(NFinityParse.Compare_statementContext compare_statement : and_statement.compare_statement()) {
                    if(getReturnType(compare_statement) != CurrentProject.Assembly.Bool) {
                        throw new BadNTypeException("All conditional statements must be boolean");
                    }
                }
            }

            return CurrentProject.Assembly.Bool;
        } else {
            //Otherwise, our return type is just the sub-statement return type
            return getReturnType(and_statement.compare_statement(0));
        }
    }

    public static NType getReturnType(NFinityParse.Compare_statementContext compare_statement) throws BadNTypeException {
        if(compare_statement.low_statement().size() > 1) {
            if (CurrentProject.Preferences.RequireBoolean) {
                for (NFinityParse.Low_statementContext low_statement : compare_statement.low_statement()) {
                    if (getReturnType(low_statement) != CurrentProject.Assembly.Bool) {
                        throw new BadNTypeException("All conditional statements must be boolean");
                    }
                }
            }

            return CurrentProject.Assembly.Bool;
        } else {
            return getReturnType(compare_statement.low_statement(0));
        }
    }

    public static NType getReturnType(NFinityParse.Low_statementContext low_statement) throws BadNTypeException {
        if(low_statement.med_statement().size() > 1) {
            if (CurrentProject.Preferences.RequireBoolean) {
                for (NFinityParse.Med_statementContext med_statement : low_statement.med_statement()) {
                    if (getReturnType(med_statement) != CurrentProject.Assembly.Bool) {
                        throw new BadNTypeException("All conditional statements must be boolean");
                    }
                }
            }

            return CurrentProject.Assembly.Bool;
        } else {
            return getReturnType(low_statement.med_statement(0));
        }
    }

    public static NType getReturnType(NFinityParse.Med_statementContext med_statement) throws BadNTypeException {
        if(med_statement.high_statement().size() > 1) {
            if (CurrentProject.Preferences.RequireBoolean) {
                for (NFinityParse.High_statementContext high_statement : med_statement.high_statement()) {
                    if (getReturnType(high_statement) != CurrentProject.Assembly.Bool) {
                        throw new BadNTypeException("All conditional statements must be boolean");
                    }
                }
            }

            return CurrentProject.Assembly.Bool;
        } else {
            return getReturnType(med_statement.high_statement(0));
        }
    }

    public static NType getReturnType(NFinityParse.High_statementContext high_statement) throws BadNTypeException {
        if(high_statement.unary_statement().size() > 1) {
            if (CurrentProject.Preferences.RequireBoolean) {
                for (NFinityParse.Unary_statementContext unary_statement : high_statement.unary_statement()) {
                    if (getReturnType(unary_statement) != CurrentProject.Assembly.Bool) {
                        throw new BadNTypeException("All conditional statements must be boolean");
                    }
                }
            }

            return CurrentProject.Assembly.Bool;
        } else {
            return getReturnType(high_statement.unary_statement(0));
        }
    }

    public static NType getReturnType(NFinityParse.Unary_statementContext unary_statement) throws BadNTypeException {
        if(unary_statement.UNARY_POST() != null) {
            //TODO: unary operator processing
            return getReturnType(unary_statement.single_statement());
        } else if(unary_statement.UNARY_PRE() != null) {
            return getReturnType(unary_statement.single_statement());
        } else {
            return getReturnType(unary_statement.single_statement());
        }
    }

    public static NType getReturnType(NFinityParse.Single_statementContext single_statement) throws BadNTypeException {
        NFinityParse.Bare_valueContext bare_value = single_statement.bare_value();

        if(bare_value != null) {
            return getReturnType(bare_value);
        }

        if(single_statement.NEW() != null) {
            NFinityParse.Single_statementContext type_statement = single_statement.single_statement();

            NType statementType = getReturnType(type_statement);

            if(statementType == CurrentProject.Assembly.Typepath) {
                if(type_statement.bare_value() != null) {
                    try {
                        return CurrentProject.Assembly.getTypeInPath(type_statement.bare_value().typepath().getText());
                    } catch (NTypeNotFoundException e) {
                        throw new BadNTypeException("Could not initialise the type " + type_statement.bare_value().typepath().getText());
                    }
                } else {
                    return CurrentProject.Assembly.Any;
                }
            } else {
                //A new thing is just of type thing
                return statementType;
            }
        }

        NFinityParse.StatementContext inner_statement = single_statement.statement();

        if(inner_statement != null) {
            return getReturnType(inner_statement);
        }

        NFinityParse.Access_pathContext access_path = single_statement.access_path();

        if(access_path != null) {
            return resolveAccessType(access_path);
        }

        throw new BadNTypeException("Could not resolve type for single-statement " + single_statement.getText());
    }

    public static NType getReturnType(NFinityParse.Bare_valueContext bare_value) throws BadNTypeException {
        if(bare_value.NULL() != null) {
            return Null.Null;
        }

        if(bare_value.STRING() != null) {
            return CurrentProject.Assembly.String;
        }

        if(bare_value.NUM() != null || bare_value.BINARY() != null) {
            return CurrentProject.Assembly.Num;
        }

        if(bare_value.typepath() != null) {
            return CurrentProject.Assembly.Typepath;
        }

        throw new BadNTypeException("Could not find a type corresponding to bare value " + bare_value.getText());
    }

    public static NType resolveAccessType(NFinityParse.Access_pathContext access) throws BadNTypeException {
        NContextDiver pathDiver;

        NFinityParse.Access_startContext access_start = access.access_start();

        if(access_start != null) {
            NFinityParse.Bare_valueContext bare_value = access_start.bare_value();
            if(bare_value != null) {
                pathDiver = new NContextDiver(getReturnType(bare_value).TypeContext);
            } else if (access_start.SRC() != null) {
                //SRC is type-based, so doing src.something specifies the scope of the type
                //This scope is found by the current content's getType() typecontext
                pathDiver = new NContextDiver(CurrentProject.ContextDiver.currentContext().getType().TypeContext);
            } else {
                pathDiver = new NContextDiver(getReturnType(access_start.statement()).TypeContext);
            }

        } else {
            pathDiver = new NContextDiver(CurrentProject.ContextDiver.currentContext());
        }

        NType currentType = pathDiver.currentContext().getType();

        for(NFinityParse.Access_partContext access_part : access.access_part()) {
            NFinityParse.Member_nameContext member_name = access_part.member_name();

            NSigRequest sigRequest;

            if(member_name != null) {
                sigRequest = new NSigRequest(member_name.getText());
            } else {
                NFinityParse.Method_callContext method_call = access_part.method_call();

                //TODO: Allow the empty argument i.e. ,,
                List<NArgRequest> args = new ArrayList<NArgRequest>();

                for(NFinityParse.ArgumentContext argument : method_call.arguments().argument()) {
                    NFinityParse.Member_nameContext arg_name = argument.member_name();

                    NArgRequest generatedArg;

                    if(arg_name != null) {
                        generatedArg = new NArgRequest(arg_name.getText(), getReturnType(argument.statement()));
                    } else {
                        generatedArg = new NArgRequest(getReturnType(argument.statement()));
                    }

                    args.add(generatedArg);
                }

                sigRequest = new NSigRequest(method_call.member_name().getText(), args.toArray(new NArgRequest[args.size()]));
            }

            try {
                currentType = pathDiver.currentContext().resolveMember(sigRequest).Signature.ReturnType;
                pathDiver.diveInto(currentType.TypeContext);
            }
            catch (NSigCannotResolveException e) {
                error(FilePath, LineNumber, e.Message());

                throw new BadNTypeException("Could not resolve type for bad signature");
            }
        }

        return currentType;
    }


    /**
     * Add warning message to the assembler storage to be printed later - warning messages should not prevent compilation
     * @param filepath
     * @param linecount
     * @param message
     */
    public static void warn(Path filepath, int linecount, String message) {
        _messages.add(String.format("WARNING: File {0}: Line {1}, {2}", filepath.toString(), linecount, message));
    }

    /**
     * Add error message to the assembler to be printed later
     * @param filepath
     * @param linecount
     * @param message
     */
    public static void error(Path filepath, int linecount, String message) {
        _messages.add(String.format("ERROR: File {0}: Line {1}, {2}", filepath.toString(), linecount, message));
    }
    
    
    public static Iterable<String> getMessages() {
        List<String> messageCopy = ListUtils.copy(_messages);
        _messages.clear();
        return messageCopy;
    }
}
