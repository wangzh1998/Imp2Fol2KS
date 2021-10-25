import java.util.ArrayList;
import java.util.HashMap;

class KSStructure {
    ArrayList<Integer> domainList = new ArrayList<>();
    ArrayList<String> variableList = new ArrayList<>();
    ArrayList<KSNode> initialStateList = new ArrayList<>();
    ArrayList<Transition> transitionList = new ArrayList<>();
}

class Transition {
    HashMap<String, String> pcMap = new HashMap<>();
    HashMap<String, String> nextPcMap = new HashMap<>();
    String condition = new String();
    ArrayList<String> exprList = new ArrayList<>();
}


class  KSNode {
    static int count = 0;
    String key = new String();

    HashMap<String, String> pcMap = new HashMap<>();
    HashMap<String, String> labelMap = new HashMap<>();

    boolean selfCycle = false;
    ArrayList<KSNode>  nextNodeList = new ArrayList<>();

    void KSNode(){
        key = String.valueOf(count++);
    }
}
