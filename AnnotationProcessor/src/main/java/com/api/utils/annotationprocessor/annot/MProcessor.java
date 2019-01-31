package com.api.utils.annotationprocessor.annot;

//import com.sun.source.util.Trees;
//import com.sun.tools.javac.code.Flags;
//import com.sun.tools.javac.processing.JavacProcessingEnvironment;
//import com.sun.tools.javac.tree.JCTree;
//import com.sun.tools.javac.tree.TreeMaker;
//import com.sun.tools.javac.tree.TreeTranslator;
//import com.sun.tools.javac.util.Context;
//import com.sun.tools.javac.util.List;
//import com.sun.tools.javac.util.Name;
//import com.sun.tools.javac.util.Names;

import java.lang.reflect.Modifier;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import static jdk.nashorn.internal.objects.Global.println;

/**
 * Created by fj on 2018/8/13.
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"com.api.utils.annotationprocessor.annot.AntiBean"})
public class MProcessor extends AbstractProcessor {
//    private Trees trees;
//    private TreeMaker make;
//    private Names names;
//    private Context context;
    private Messager messager;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
//        trees = Trees.instance(processingEnv);
//        context = ((JavacProcessingEnvironment)processingEnv).getContext();
//        make = TreeMaker.instance(context);
//        names = Names.instance(context);
        messager.printMessage(Diagnostic.Kind.NOTE, "----------init----------");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        /*messager.printMessage(Diagnostic.Kind.NOTE, "----------start----------"+roundEnv.processingOver());
        if (!roundEnv.processingOver()) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(AntiBean.class );
            for (Element each : elements) {
//                ElementKind.CLASS
//                if (each.getKind() == ElementKind.CLASS) {
                    messager.printMessage(Diagnostic.Kind.NOTE, " assertions getKind " + each.getKind());
//                    messager.printMessage(Diagnostic.Kind.NOTE, " assertions inlined class " + ((TypeElement) each).getQualifiedName().toString() );
                    JCTree tree = (JCTree) trees.getTree(each);
                    tree.accept(new TreeTranslator(){
                        @Override
                        public void visitSelect(JCTree.JCFieldAccess jcFieldAccess) {
                            messager.printMessage(Diagnostic.Kind.NOTE, "jcFieldAccess ==  "+jcFieldAccess);
                            super.visitSelect(jcFieldAccess);
                        }

                        @Override
                        public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                            super.visitClassDef(jcClassDecl);
                        }

                        @Override
                        public void visitMethodDef(JCTree.JCMethodDecl jcMethodDecl) {
                            super.visitMethodDef(jcMethodDecl);
                            messager.printMessage(Diagnostic.Kind.NOTE, "  visitMethodDef  "+jcMethodDecl.getName()+" ,Tag="+jcMethodDecl.getTag()+" ,type="+jcMethodDecl.type);
                            messager.printMessage(Diagnostic.Kind.NOTE, "  visitMethodDef  "+jcMethodDecl.getBody().getStatements().toString());
                            messager.printMessage(Diagnostic.Kind.NOTE, "  visitMethodDef  "+jcMethodDecl.getBody().stats.length());
//                            if (jcMethodDecl.getName().toString().equals("getUserName")) {
//                                JCTree.JCMethodDecl methodDecl = make.MethodDef(jcMethodDecl.getModifiers(), names.fromString( "testMethod" ), jcMethodDecl.restype, jcMethodDecl.getTypeParameters(), jcMethodDecl.getParameters(), jcMethodDecl.getThrows(), jcMethodDecl.getBody(), jcMethodDecl.defaultValue );
//                                result = methodDecl;
//                            }
                            List<JCTree.JCStatement> stats = jcMethodDecl.getBody().stats;
                            for (JCTree.JCStatement stat : stats) {
                                if (stat.toString().contains("hggg")) {

                                }
                                messager.printMessage(Diagnostic.Kind.NOTE, "  visitMethodDef  Kind="+stat.getKind()+" ,Tag="+stat.getTag()+" -- "+stat.toString()+" -pos "+stat);
                            }
                            List<JCTree.JCStatement> statements = jcMethodDecl.getBody().getStatements();
                            for (JCTree.JCStatement statement : statements) {
                                messager.printMessage(Diagnostic.Kind.NOTE, "  visitMethodDef  Kind="+statement.getKind()+" ,Tag="+statement.getTag()+" -- "+statement.toString()+" -pos "+statement.pos+" -pos "+statement.getPreferredPosition());
                            }

                            jcMethodDecl.name = names.fromString(jcMethodDecl.getName()+"_test");
                            messager.printMessage(Diagnostic.Kind.NOTE, "  22 visitMethodDef  "+jcMethodDecl.getName());
                            this.result = jcMethodDecl;
                        }

//                        visit

                        @Override
                        public void visitVarDef(JCTree.JCVariableDecl jcVariableDecl) {
                            super.visitVarDef(jcVariableDecl);

                            messager.printMessage(Diagnostic.Kind.NOTE, "  visitVarDef  "+jcVariableDecl.getName()+"  ==  "+jcVariableDecl.getInitializer()+"  - "+jcVariableDecl.getModifiers().flags);
//                            if (jcVariableDecl.getModifiers().flags != 0){
                                jcVariableDecl.name = names.fromString(jcVariableDecl.getName()+"_test");
                                messager.printMessage(Diagnostic.Kind.NOTE, "  22 visitVarDef  "+jcVariableDecl.getName());
                                this.result = jcVariableDecl;
//                            }
                        }

                        @Override
                        public void visitLiteral(JCTree.JCLiteral jcLiteral) {
                            super.visitLiteral(jcLiteral);
                            messager.printMessage(Diagnostic.Kind.NOTE, "  jcLiteral  "+jcLiteral.getValue()+"  =typetag=  "+jcLiteral.typetag+" value- "+jcLiteral.value+" Kind- "+jcLiteral.getKind()+" Tag- "+jcLiteral.getTag());
                        }

                    });
//                    return true;
//                }
            }
        } else
            messager.printMessage(Diagnostic.Kind.NOTE, " assertions inlined." );
        */return false;
    }

}
