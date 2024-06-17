package com.example.classannotation.processor;

import com.example.classannotation.annotations.SSTLogger;
import com.sun.source.util.Trees;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import com.google.auto.service.AutoService;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes("com.example.classannotation.annotations.SSTLoggers")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class SSTLoggerProcessor extends AbstractProcessor {
    private Trees trees;
    private TreeMaker treeMaker;
    private Names names;
    private JavacElements elements;

    @Override
    public synchronized void init(javax.annotation.processing.ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        trees = JavacTrees.instance(processingEnv);
        elements = (JavacElements) processingEnv.getElementUtils();
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        treeMaker = TreeMaker.instance(context);
        names = Names.instance(context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(SSTLogger.class)) {
            if (element instanceof TypeElement typeElement) {
                JCTree tree = (JCTree) trees.getTree(typeElement);
                injectLogger(typeElement, tree);
            }
        }
        return true;
    }

    private void injectLogger(TypeElement typeElement, JCTree tree) {
        tree.accept(new JCTree.Visitor() {
            @Override
            public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                // Create the logger field
                JCTree.JCVariableDecl loggerField = makeLoggerField(typeElement);
                jcClassDecl.defs = jcClassDecl.defs.prepend(loggerField);

//                // Add methods from SstLogger
//                for (Element member : elements.getAllMembers(elements.getTypeElement("com.example.classannotation.service.SstLogger"))) {
//                    if (member instanceof Symbol.MethodSymbol && !member.getSimpleName().contentEquals("<init>")) {
//                        JCTree.JCMethodDecl methodDecl = makeMethodDecl((Symbol.MethodSymbol) member, loggerField);
//                        jcClassDecl.defs = jcClassDecl.defs.prepend(methodDecl);
//                    }
//                }
            }
        });
    }

    private JCTree.JCVariableDecl makeLoggerField(TypeElement typeElement) {
        Symbol.VarSymbol loggerSymbol = new Symbol.VarSymbol(0, names.fromString("logger"),
                new Type.ClassType(Type.noType, List.nil(), elements.getTypeElement("com.example.classannotation.service.SstLogger")),
                (Symbol) typeElement);
        return treeMaker.VarDef(loggerSymbol,
                treeMaker.NewClass(null,
                        List.nil(),
                        treeMaker.Ident(elements.getName("SstLogger")),
                        List.of(treeMaker.Literal(typeElement.getSimpleName().toString())),
                        null));
    }

    private JCTree.JCMethodDecl makeMethodDecl(Symbol.MethodSymbol methodSymbol, JCTree.JCVariableDecl loggerField) {
        List<JCTree.JCVariableDecl> params = List.nil();

        for (Symbol.VarSymbol param : methodSymbol.getParameters()) {
            // Create a variable declaration for the parameter
            JCTree.JCVariableDecl paramDecl = treeMaker.VarDef(
                    treeMaker.Modifiers(Flags.PARAMETER),
                    names.fromString(param.name.toString()),
                    treeMaker.Type(param.type),
                    null // Initialize expression, which is null for parameters
            );
            // Append the parameter declaration to the list of parameters
            params = params.append(paramDecl);
        }

        JCTree.JCExpression methodInvocation = treeMaker.Apply(
                List.nil(),
                treeMaker.Select(treeMaker.Ident(loggerField), methodSymbol.name),
                List.from(params.map(param -> treeMaker.Ident(param.name)))
        );
        JCTree.JCBlock body = treeMaker.Block(0, List.of(treeMaker.Exec(methodInvocation)));

        // Create the method declaration
        return treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC),
                names.fromString(methodSymbol.getSimpleName().toString()),
                treeMaker.Type(methodSymbol.getReturnType()),
                List.nil(),
                params,
                List.nil(),
                body,
                null
        );
    }

//    private Trees trees;
//    private TreeMaker make;
//    private Name.Table names;
//    private Resolve rs;
//    private JavacElements elements;
//
//    @Override
//    public synchronized void init(ProcessingEnvironment env) {
//        super.init(env);
//        trees = Trees.instance(env);
//        Context context = ((JavacProcessingEnvironment) env).getContext();
//        make = TreeMaker.instance(context);
//        names = Names.instance(context).table;
//        rs = Resolve.instance(context);
//        elements = JavacElements.instance(context);
//    }
//
//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        annotations.forEach(annotation -> {
//            roundEnv.getElementsAnnotatedWith(annotation).forEach(element -> {
//                if (annotation.getQualifiedName().toString().equals("com.example.classannotation.SSTLogger")) {
//                    processSSTLogger((TypeElement) element);
//                }
//            });
//        });
//        return true;
//    }
//
//    private void processSSTLogger(TypeElement element) {
//        JCTree.JCClassDecl classDecl = (JCTree.JCClassDecl) trees.getTree(element);
//        List<JCTree> newMethods = generateMethodsFromSstLogger();
//        classDecl.defs = classDecl.defs.appendList(newMethods);
//    }
//
//    private List<JCTree> generateMethodsFromSstLogger() {
//        String sstLoggerClassName = "com.example.classannotation.service.SstLogger";
//        Symbol.ClassSymbol sstLoggerClass = (Symbol.ClassSymbol) findSymbol(sstLoggerClassName);
//
//        List<JCTree> methods = List.nil();
//        for (Symbol member : sstLoggerClass.getEnclosedElements()) {
//            if (member instanceof Symbol.MethodSymbol) {
//                Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol) member;
//                if (methodSymbol.isConstructor()) continue;
//                JCTree.JCMethodDecl methodDecl = make.MethodDef(methodSymbol, null);
//                methods = methods.append(methodDecl);
//            }
//        }
//
//        return methods;
//    }
//
//    private Symbol findSymbol(String className) {
//        Symbol classSymbol = elements.getTypeElement(className);
//        if (classSymbol == null) throw new IllegalStateException("findSymbol: couldn't find symbol " + className);
//        return classSymbol;
//    }
}
