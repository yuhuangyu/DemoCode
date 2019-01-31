package com.api.utils.annotationprocessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * Created by fj on 2018/8/2.
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"com.api.utils.annotationprocessor.CompileAnnotation", "com.api.utils.annotationprocessor.Interface"})
public class AnnotationCompileProcessor extends AbstractProcessor {
    private Messager messager;
    private Filer filer;
    private Types typeUtils;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        if (roundEnvironment.processingOver()) {
            return false;
        }
        Map<String, AnnotatedClass> classMap = new HashMap<>();

        messager.printMessage(Diagnostic.Kind.NOTE, "----------start----------"+roundEnvironment.processingOver());
        /*for (TypeElement annotation : annotations) {
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(annotation);
            for (Element element : elements) {
                if (element.getKind() != ElementKind.FIELD) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "Only FIELD can be annotated with AnnotationInfo");
                    return true;
                }
                CompileAnnotation annotationInfo = element.getAnnotation(CompileAnnotation.class);
                int value = annotationInfo.value();
                messager.printMessage(Diagnostic.Kind.NOTE, "value: " + value);
            }
        }*/

        messager.printMessage(Diagnostic.Kind.NOTE, "----------AnnotatedMethod----------");
        // 得到所有注解@Interface的Element集合
        Set<? extends Element> elementSet = roundEnvironment.getElementsAnnotatedWith(Interface.class);

        for (Element e : elementSet) {
            if (e.getKind() != ElementKind.METHOD) {
                error(e, "错误的注解类型，只有函数能够被该 @%s 注解处理", Interface.class.getSimpleName());
                return true;
            }

            ExecutableElement element = (ExecutableElement) e;
            AnnotatedMethod annotatedMethod = new AnnotatedMethod(element);

            String classname = annotatedMethod.getSimpleClassName();
            AnnotatedClass annotatedClass = classMap.get(classname);
            if (annotatedClass == null) {
                PackageElement pkg = elementUtils.getPackageOf(element);
                annotatedClass = new AnnotatedClass(pkg.getQualifiedName().toString(), element.getAnnotation(Interface.class).value());
                annotatedClass.addMethod(annotatedMethod);
                classMap.put(classname, annotatedClass);
            } else
                annotatedClass.addMethod(annotatedMethod);

        }
        // 代码生成
        for (AnnotatedClass annotatedClass : classMap.values()) {
            annotatedClass.generateCode(elementUtils, filer);
        }
        return true;

    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }
}
