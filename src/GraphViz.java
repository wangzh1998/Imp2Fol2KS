import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

class  GraphViz{
    private String runPath = "";
    private String dotPath = "";
    private String runOrder="";
    private String dotCodeFile="dotcode.txt";
    private String resultGif="dotGif";
    private StringBuilder graph = new StringBuilder();

    Runtime runtime=Runtime.getRuntime();

    public static void main(String[] args) {
        GraphViz gViz=new GraphViz("D:\\Users\\lenovo\\Desktop\\eee", "D:\\LenovoQMDownload\\SoftMgr\\Graphviz\\bin\\dot.exe");
        gViz.start_graph();
        gViz.addln("munknownunknown1 -> B ;");
        gViz.addln("A->A;");
        gViz.addln("A->C;");
        gViz.addln("C->B;");
        gViz.addln("B->D;");
        gViz.addln("C->E;");
        gViz.addln("munknownunknown1->unknownl0l1;");
        gViz.addln("unknownl0l1->munknownunknown;");
        gViz.end_graph();
        try {
            gViz.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        File file=new File(runPath);
        file.mkdirs();
        writeGraphToFile(graph.toString(), runPath);
        creatOrder();
        try {
            runtime.exec(runOrder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void creatOrder(){
        runOrder+=dotPath+" ";
        runOrder+=runPath;
        runOrder+="\\"+dotCodeFile+" ";
        runOrder+="-T gif ";
        runOrder+="-o ";
        runOrder+=runPath;
        runOrder+="\\"+resultGif+".gif";
        System.out.println(runOrder);
    }

    public void writeGraphToFile(String dotcode, String filename) {
        try {
            File file = new File(filename+"\\"+dotCodeFile);
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(dotcode.getBytes());
            fos.close();
        } catch (java.io.IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public GraphViz(String runPath,String dotPath) {
        this.runPath=runPath;
        this.dotPath=dotPath;
    }

    public void add(String line) {
        graph.append("\t"+line);
    }

    public void addln(String line) {
        graph.append("\t"+line + "\n");
    }

    public void addln() {
        graph.append('\n');
    }

    public void addLabel(String name, String label){
        graph.append("\t" + name + "[label=\"" + label + "\"]\n");
    }

    public void start_graph() {
        graph.append("digraph G {\n") ;
    }

    public void end_graph() {
        graph.append("}") ;
    }
}