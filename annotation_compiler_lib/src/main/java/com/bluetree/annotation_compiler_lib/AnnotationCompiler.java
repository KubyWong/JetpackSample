package com.bluetree.annotation_compiler_lib;

import com.bluetree.annotation_lib.BindView;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.tools.JavaFileObject;

/**
 * 注解处理器 生成Activity对应的类来绑定view
 */
@AutoService(Processor.class)//必须通过该注解来注册注解处理器，否则无效
public class AnnotationCompiler extends AbstractProcessor {

    //生成文件的对象
    Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
    }

    /**
     * 声明这个注解处理器需要处理的注解
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(BindView.class.getCanonicalName());
        return types;
    }

    /**
     * 声明当情注解处理器支持的java版本
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    /**
     * 在这个方法里生成我们需要的文件
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {


        //拿到应用里面所有用到BindView的节点，
        //关于节点，有类节点，成员变量节点，方法节点，一个树形结构来的
        Set<? extends Element> elementsAnnotation = roundEnvironment.getElementsAnnotatedWith(BindView.class);

        //将所有的节点根据不同的文件分开
        Map<String, List<Element>> map = new HashMap<>();


        for (Element element :
                elementsAnnotation) {
            //获取成员变量的节点
            VariableElement variableElement = (VariableElement) element;

            //获取类节点
            Element elementClass = variableElement.getEnclosingElement();
            String className = elementClass.getSimpleName().toString();
            if (map.get(className) == null) {
                map.put(className, new ArrayList<Element>());
            }
            map.get(className).add(variableElement);
        }

        if (map.size() > 0) {
            //k开始写文件
            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                String activityName = iterator.next();
                List<Element> variableElements = map.get(activityName);

                //通过获取成员变量的节点获取到上一个节点，也就是类节点
                TypeElement enclosingElement = (TypeElement) variableElements.get(0).getEnclosingElement();
                //通过类节点，获取到类的包名
                String packageName = processingEnv.getElementUtils().getPackageOf(enclosingElement).toString();

                Writer writer = null;
                //创建文件
                try {
                    JavaFileObject sourceFile
                            = filer.createSourceFile(packageName + "." + activityName + "_ViewBinding");

                     writer = sourceFile.openWriter();

                    writer.write("package "+packageName+";\n");
                    writer.write("import com.bluetree.annotation_lib.IBind;\n");
                    writer.write("public class "+activityName+"_ViewBinding implements IBind<"+packageName + "." + activityName+"> {\n");
                    writer.write("  @Override\n");
                    writer.write("  public void bind("+packageName + "." + activityName+" target) {\n");

                    for (Element varableEle :
                            variableElements) {

                        //获取变量名
                        String varableName = varableEle.getSimpleName().toString();
                        //获取id
                        int id = varableEle.getAnnotation(BindView.class).value();
                        //获取变量类型
                        TypeMirror typeMorror = varableEle.asType();


                        writer.write("   target."+varableName+" = ("+typeMorror+")target.findViewById("+id+");\n");
                    }
                    writer.write("  }\n");
                    writer.write("}\n");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    if(writer!=null){
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }





        return false;
    }
}
