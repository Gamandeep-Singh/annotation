package com.example.classannotation.processor;

import com.example.classannotation.annotations.SSTLogger;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Set;


@SupportedAnnotationTypes("com.example.classannotation.annotations.SSTLogger")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class GreetingMethodProcessor extends AbstractProcessor {

    private Trees trees;
    private TreeMaker treeMaker;
    private Names names;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        trees = Trees.instance(processingEnv);
        Context context = ((com.sun.tools.javac.processing.JavacProcessingEnvironment) processingEnv).getContext();
        treeMaker = TreeMaker.instance(context);
        names = Names.instance(context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(SSTLogger.class)) {
            if (element instanceof TypeElement typeElement) {
                JCTree tree = (JCTree) trees.getTree(typeElement);
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing Annotation @SSTLogger for class: " + typeElement);
                if (tree instanceof JCTree.JCClassDecl) {
                    JCTree.JCClassDecl classDecl = (JCTree.JCClassDecl) tree;
                    addGreetMethod(classDecl);
                    addSstLoggerField(classDecl);

//                    //Re-Writing Class
//                    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//                    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
//                    StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
//
//                    // Prepare a compilation unit
//                    JavaFileObject compilationUnit = new StringJavaFileObject(typeElement.getQualifiedName().toString(), classDecl.toString());
//                    Iterable<? extends JavaFileObject> compilationUnits = Collections.singletonList(compilationUnit);
//                    Iterable<String> classes = Collections.singletonList(typeElement.getQualifiedName().toString());
//                    Iterable<String> options = Collections.singletonList("-proc:none");
//
//                    // Parse the compilation unit
//                    JavacTask task = (JavacTask) compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);
////                    JavacTask task = (JavacTask) compiler.getTask(null, fileManager, diagnostics, options, classes, compilationUnits);
//                    task.call();
//                    try {
//                        fileManager.close();
//                    } catch (IOException e) {
//                        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, " Error : " + e.getMessage());
//                    }
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, " Annotation @SSTLogger processing done for " + typeElement);
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, " final class: " + classDecl);
                }
            }
        }
        return true;
    }

    private void addSstLoggerField(JCTree.JCClassDecl classDecl) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, " @GreetingMethodProcessor:addSstLoggerField: Create the sstlogger field");
        // Create the field: private final com.example.classannotation.service.SstLogger sstlogger
        // = new com.example.classannotation.service.SstLogger(this.getClass().getName());
        JCTree.JCExpression fieldType = treeMaker.Ident(names.fromString("com.example.loggerclassannotation.loggerservice.SstLogger"));

        // Generate getClass().getName() call
        JCTree.JCExpression getClassCall = treeMaker.Apply(
                List.nil(),
                treeMaker.Select(treeMaker.Ident(names.fromString("this")), names.fromString("getClass")),
                List.nil()
        );

        JCTree.JCExpression getNameCall = treeMaker.Apply(
                List.nil(),
                treeMaker.Select(getClassCall, names.fromString("getName")),
                List.nil()
        );

        // Generate new SstLogger(getClass().getName()) call
        JCTree.JCExpression initExpression = treeMaker.NewClass(
                null,
                List.nil(),
                treeMaker.Ident(names.fromString("com.example.loggerclassannotation.loggerservice.SstLogger")),
                List.of(getNameCall),
                null
        );

        JCTree.JCVariableDecl sstloggerField = treeMaker.VarDef(
                treeMaker.Modifiers(com.sun.tools.javac.code.Flags.PRIVATE | com.sun.tools.javac.code.Flags.FINAL),
                names.fromString("sstlogger"),
                fieldType,
                initExpression
        );
        classDecl.defs = classDecl.defs.prepend(sstloggerField);
    }

    private void addGreetMethod(JCTree.JCClassDecl classDecl) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, " @GreetingMethodProcessor:addGreetMethod: Create the method body");
        // Create the method body: return "Hello, World!";
        JCTree.JCExpression returnType = treeMaker.TypeIdent(com.sun.tools.javac.code.TypeTag.VOID);
        JCTree.JCBlock body = treeMaker.Block(0, List.of(
                treeMaker.Exec(
                        treeMaker.Apply(
                                List.nil(),
                                treeMaker.Select(
                                        treeMaker.Select(treeMaker.Ident(names.fromString("System")), names.fromString("out")),
                                        names.fromString("println")
                                ),
                                List.of(treeMaker.Literal("Hello, World!"))
                        )
                )
        ));
        JCTree.JCMethodDecl greetMethod = treeMaker.MethodDef(
                treeMaker.Modifiers(com.sun.tools.javac.code.Flags.PUBLIC),
                names.fromString("greet"),
                returnType,
                List.nil(),
                List.nil(),
                List.nil(),
                body,
                null
        );

        classDecl.defs = classDecl.defs.append(greetMethod);
        List<JCTree> newDefs = classDecl.defs;
        List.convert(JCTree.class, newDefs);
    }

    static class StringJavaFileObject extends SimpleJavaFileObject {
        private final String sourceCode;

        protected StringJavaFileObject(String className, String sourceCode) {
            super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.sourceCode = sourceCode;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return sourceCode;
        }
    }
}
