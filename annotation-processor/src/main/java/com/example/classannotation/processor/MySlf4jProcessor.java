//package com.example.classannotation.processor;
//
//import javax.annotation.processing.AbstractProcessor;
//import javax.annotation.processing.Processor;
//import javax.annotation.processing.RoundEnvironment;
//import javax.annotation.processing.SupportedAnnotationTypes;
//import javax.annotation.processing.SupportedSourceVersion;
//import javax.lang.model.SourceVersion;
//import javax.lang.model.element.Element;
//import javax.lang.model.element.TypeElement;
//import javax.tools.Diagnostic;
//import javax.tools.FileObject;
//import javax.tools.JavaFileObject;
//
//import com.example.classannotation.annotations.SSTLogger;
//import com.squareup.javapoet.JavaFile;
//import com.squareup.javapoet.FieldSpec;
//import com.squareup.javapoet.TypeSpec;
//import com.squareup.javapoet.ClassName;
//
//import java.io.IOException;
//import java.io.Writer;
//import java.util.Set;
//import javax.lang.model.element.Modifier;
//import javax.lang.model.util.Elements;
//
//@SupportedAnnotationTypes("com.example.classannotation.annotations.SSTLoggers")
//@SupportedSourceVersion(SourceVersion.RELEASE_17)
//public class MySlf4jProcessor extends AbstractProcessor {
//    private Elements elementUtils;
//
//    @Override
//    public synchronized void init(javax.annotation.processing.ProcessingEnvironment processingEnv) {
//        super.init(processingEnv);
//        elementUtils = processingEnv.getElementUtils();
//    }
//
//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        for (Element element : roundEnv.getElementsAnnotatedWith(SSTLogger.class)) {
//            if (element instanceof TypeElement) {
//                TypeElement typeElement = (TypeElement) element;
//                try {
//                    generateLoggerField(typeElement);
//                } catch (IOException e) {
//                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.toString());
//                }
//            }
//        }
//        return true;
//    }
//
//    private void generateLoggerField(TypeElement typeElement) throws IOException {
//        String className = typeElement.getSimpleName().toString();
//        String packageName = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
//
//        String fullClassName = packageName + "." + className;
//
//        // Check if the file already exists
//        try {
//            FileObject existingFile = processingEnv.getFiler().getResource(javax.tools.StandardLocation.SOURCE_OUTPUT, packageName, className + ".java");
//            existingFile.openInputStream().close(); // If this succeeds, the file exists
//            return; // No need to generate the file again
//        } catch (IOException e) {
//            // File does not exist, continue to generate it
//        }
//
//        // Define the logger field
//        FieldSpec loggerField = FieldSpec.builder(
//                        ClassName.get("org.slf4j", "Logger"),
//                        "log",
//                        Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
//                .initializer("$T.getLogger($L.class)", ClassName.get("org.slf4j", "LoggerFactory"), fullClassName + ".class")
//                .build();
//
//        // Create the new class with the logger field
//        TypeSpec newClass = TypeSpec.classBuilder(className)
//                .addModifiers(Modifier.PUBLIC)
//                .addField(loggerField)
//                .build();
//
//        // Write the new class to a file
//        JavaFile javaFile = JavaFile.builder(packageName, newClass)
//                .build();
//
//        try (Writer writer = processingEnv.getFiler().createSourceFile(fullClassName).openWriter()) {
//            javaFile.writeTo(writer);
//        }
//    }
//}
