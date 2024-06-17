package com.example.classannotation.processor;

import com.example.classannotation.annotations.MyLogger;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes("com.example.classannotation.annotations.MyLogger")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class MyLoggerProcessor extends AbstractProcessor {
    /**
     * Processes the annotations of the specified type on the elements and prints the names
     * of all classes on which the annotation is applied.
     *
     * @param annotations the annotation types requested to be processed
     * @param roundEnv    environment for information about the current and prior round
     * @return boolean: whether the set of annotation types are claimed by this processor
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // Iterate over all elements annotated with @SSTLogger
        for (Element element : roundEnv.getElementsAnnotatedWith(MyLogger.class)) {
            // Check if the element is a class
            if (element instanceof TypeElement) {
                TypeElement typeElement = (TypeElement) element;
                // Get the simple name of the class and print it
                String className = typeElement.getSimpleName().toString();
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Class annotated with @MyLogger: " + className);
            }
        }
        return true;
    }
}
