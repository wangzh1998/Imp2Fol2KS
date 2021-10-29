import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class KSStructure {

    public static final String ENTER = "m";
    public static final String END = "m'";
    public static final String UNKNOWN = "unknown";

    // expr中不允许出现括号 expr中的每一条是单个语句
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String EQUAL = "==";
    public static final String ASSIGN = ":=";
    public static final String NOT_EQUAL = "!=";
    public static final String LESS_EQUAL = "<=";
    public static final String NOT = "!";
    public static final String AND = "and";
    public static final String OR = "or";
    public static final String ADD = "+";
    public static final String SUB = "-";
    public static final String MUL = "*";
    public static final String SKIP = "skip";

    ArrayList<Integer> domainList = new ArrayList<>();
    ArrayList<String> variableList = new ArrayList<>();
    ArrayList<KSNode> initialStateList = new ArrayList<>();
    ArrayList<Transition> transitionList = new ArrayList<>();

    public KSStructure(Boolean isDemo){
        if (!isDemo) return;
        // D
        domainList.add(0);
        domainList.add(1);

        // V
        variableList.add("turn");

        // S0
        KSNode initial0 = new KSNode();
        initial0.pcMap.put("pc", ENTER);
        initial0.pcMap.put("pc0", UNKNOWN);
        initial0.pcMap.put("pc1", UNKNOWN);
        initial0.varMap.put("turn", 0);
        KSNode initial1 = new KSNode();
        initial0.pcMap.put("pc", ENTER);
        initial0.pcMap.put("pc0", UNKNOWN);
        initial0.pcMap.put("pc1", UNKNOWN);
        initial0.varMap.put("turn", 1);
        initialStateList.add(initial0);
        initialStateList.add(initial1);

        // Transition
        // 1111 最外层的进入和离开，需要同时写出所有的pc状态吗？ 需要。因为此时pc0和pc1同时发生变化。
        Transition t0 = new Transition();
        t0.pcMap.put("pc", ENTER);
        t0.pcMap.put("pc0", UNKNOWN);
        t0.pcMap.put("pc1", UNKNOWN);
        t0.nextPcMap.put("pc", UNKNOWN); //111 nextPc和Pc用同样的名字，不加'
        t0.nextPcMap.put("pc0", "l0");
        t0.nextPcMap.put("pc1", "l1");
        t0.condition = TRUE;

        Transition t1 = new Transition();
        t1.pcMap.put("pc", UNKNOWN);
        t1.pcMap.put("pc0", "l0");
        t1.pcMap.put("pc1", "l1");
        t1.nextPcMap.put("pc", END);
        t1.nextPcMap.put("pc0", UNKNOWN);
        t1.nextPcMap.put("pc1", UNKNOWN);
        t1.condition = TRUE;

        Transition t2 = new Transition();
        t2.pcMap.put("pc0", "l0");
        t2.nextPcMap.put("pc0", "NC0");
        t2.condition = TRUE;
//        t2.exprList.add("same(turn)");

        Transition t3 = new Transition();
        t3.pcMap.put("pc1", "l1");
        t3.nextPcMap.put("pc1", "NC1");
        t3.condition = TRUE;
//        t3.exprList.add("same(turn)");

        Transition t4 = new Transition();
        t4.pcMap.put("pc0", "NC0");
        t4.nextPcMap.put("pc0", "CR0");
        t4.condition = "turn = 0";
//        t4.exprList.add("same(turn)");

        Transition t5 = new Transition();
        t5.pcMap.put("pc1", "NC1");
        t5.nextPcMap.put("pc1", "CR1");
        t5.condition = "turn = 1";
//        t5.exprList.add("same(turn)");

        Transition t6 = new Transition();
        t6.pcMap.put("pc0", "NC0");
        t6.nextPcMap.put("pc0", "NC0");
        t6.condition = "turn != 0"; //1111 != 如何表示？
//        t6.exprList.add("same(turn)");

        Transition t7 = new Transition();
        t7.pcMap.put("pc1", "NC1");
        t7.nextPcMap.put("pc1", "NC1");
        t7.condition = "turn != 1"; //1111 != 如何表示？
//        t7.exprList.add("same(turn)");

        Transition t8 = new Transition();
        t8.pcMap.put("pc0", "CR0");
        t8.nextPcMap.put("pc0", "l0");
        t8.condition = TRUE; //1111 != 如何表示？
        t8.exprList.add("turn = 1");//1111 turn' 括号  ps:1.turn后面不加',直接用turn 2.不用mod (1+1)mod2

        Transition t9 = new Transition();
        t9.pcMap.put("pc1", "CR1");
        t9.nextPcMap.put("pc1", "l1");
        t9.condition = TRUE; //1111 != 如何表示？
        t9.exprList.add("turn = 0");//1111 turn' 括号

        transitionList.add(t0);
        transitionList.add(t1);
        transitionList.add(t2);
        transitionList.add(t3);
        transitionList.add(t4);
        transitionList.add(t5);
        transitionList.add(t6);
        transitionList.add(t7);
        transitionList.add(t8);
        transitionList.add(t9);

    }
//    KSStructure getDemoKS() {
//        KSStructure ksStructure = new KSStructure();
//
//        // D
//        domainList.add(0);
//        domainList.add(1);
//
//        // V
//        variableList.add("turn");
//
//        // S0
//        KSNode initial0 = new KSNode();
//        initial0.pcMap.put("pc", ENTER);
//        initial0.pcMap.put("pc0", UNKNOWN);
//        initial0.pcMap.put("pc1", UNKNOWN);
//        initial0.varMap.put("turn", 0);
//        KSNode initial1 = new KSNode();
//        initial0.pcMap.put("pc", ENTER);
//        initial0.pcMap.put("pc0", UNKNOWN);
//        initial0.pcMap.put("pc1", UNKNOWN);
//        initial0.varMap.put("turn", 1);
//        initialStateList.add(initial0);
//        initialStateList.add(initial1);
//
//        // Transition
//        // 1111 最外层的进入和离开，需要同时写出所有的pc状态吗？ 需要。因为此时pc0和pc1同时发生变化。
//        Transition t0 = new Transition();
//        t0.pcMap.put("pc", ENTER);
//        t0.pcMap.put("pc0", UNKNOWN);
//        t0.pcMap.put("pc1", UNKNOWN);
//        t0.nextPcMap.put("pc", UNKNOWN); //111 nextPc和Pc用同样的名字，不加'
//        t0.nextPcMap.put("pc0", "l0");
//        t0.nextPcMap.put("pc1", "l1");
//        t0.condition = TRUE;
//
//        Transition t1 = new Transition();
//        t1.pcMap.put("pc", UNKNOWN);
//        t1.pcMap.put("pc0", "l0");
//        t1.pcMap.put("pc1", "l1");
//        t1.nextPcMap.put("pc", END);
//        t1.nextPcMap.put("pc0", UNKNOWN);
//        t1.nextPcMap.put("pc1", UNKNOWN);
//        t1.condition = TRUE;
//
//        Transition t2 = new Transition();
//        t2.pcMap.put("pc0", "l0");
//        t2.nextPcMap.put("pc0", "NC0");
//        t2.condition = TRUE;
////        t2.exprList.add("same(turn)");
//
//        Transition t3 = new Transition();
//        t3.pcMap.put("pc1", "l1");
//        t3.nextPcMap.put("pc1", "NC1");
//        t3.condition = TRUE;
////        t3.exprList.add("same(turn)");
//
//        Transition t4 = new Transition();
//        t4.pcMap.put("pc0", "NC0");
//        t4.nextPcMap.put("pc0", "CR0");
//        t4.condition = "turn = 0";
////        t4.exprList.add("same(turn)");
//
//        Transition t5 = new Transition();
//        t5.pcMap.put("pc1", "NC1");
//        t5.nextPcMap.put("pc1", "CR1");
//        t5.condition = "turn = 1";
////        t5.exprList.add("same(turn)");
//
//        Transition t6 = new Transition();
//        t6.pcMap.put("pc0", "NC0");
//        t6.nextPcMap.put("pc0", "NC0");
//        t6.condition = "turn != 0"; //1111 != 如何表示？
////        t6.exprList.add("same(turn)");
//
//        Transition t7 = new Transition();
//        t7.pcMap.put("pc1", "NC1");
//        t7.nextPcMap.put("pc1", "NC1");
//        t7.condition = "turn != 1"; //1111 != 如何表示？
////        t7.exprList.add("same(turn)");
//
//        Transition t8 = new Transition();
//        t8.pcMap.put("pc0", "CR0");
//        t8.nextPcMap.put("pc0", "l0");
//        t8.condition = TRUE; //1111 != 如何表示？
//        t8.exprList.add("turn = 1");//1111 turn' 括号  ps:1.turn后面不加',直接用turn 2.不用mod (1+1)mod2
//
//        Transition t9 = new Transition();
//        t9.pcMap.put("pc1", "CR1");
//        t9.nextPcMap.put("pc1", "l1");
//        t9.condition = TRUE; //1111 != 如何表示？
//        t9.exprList.add("turn = 0");//1111 turn' 括号
//
//        transitionList.add(t0);
//        transitionList.add(t1);
//        transitionList.add(t2);
//        transitionList.add(t3);
//        transitionList.add(t4);
//        transitionList.add(t5);
//        transitionList.add(t6);
//        transitionList.add(t7);
//        transitionList.add(t8);
//        transitionList.add(t9);
//
//        return ksStructure;
//    }
}


class Transition {
    HashMap<String, String> pcMap = new HashMap<>();
    HashMap<String, String> nextPcMap = new HashMap<>();
    String condition = new String();
    ArrayList<String> exprList = new ArrayList<>();
}


class  KSNode {
    private static int count = 0;
    private String key = new String();
    private boolean hasChecked = false;
    private boolean selfCycle = false;

    HashMap<String, String> pcMap = new HashMap<>();
    HashMap<String, Integer> varMap = new HashMap<>();
    ArrayList<KSNode>  nextNodeList = new ArrayList<>();

    public KSNode(){
        key = String.valueOf(count++);
    }

    public String getKey() {
        return key;
    }

    public boolean isHasChecked() {
        return hasChecked;
    }

    public void setHasChecked(boolean hasChecked) {
        this.hasChecked = hasChecked;
    }

    public boolean isSelfCycle() {
        return selfCycle;
    }

    public void setSelfCycle(boolean selfCycle) {
        this.selfCycle = selfCycle;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry: this.pcMap.entrySet()) {
            stringBuilder.append(entry.getKey() + "=" + entry.getValue() + " ");
//            stringBuilder.append(entry.getValue());
        }

        for (Map.Entry<String, Integer> entry: this.varMap.entrySet()) {
            stringBuilder.append(entry.getKey() + "=" + entry.getValue() + " ");
//            stringBuilder.append(entry.getValue());
        }

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KSNode ksNode = (KSNode) o;
//        return Objects.equals(pcMap, ksNode.pcMap) && Objects.equals(varMap, ksNode.varMap) && Objects.equals(nextNodeList, ksNode.nextNodeList);
        return Objects.equals(toString(), ksNode.toString());
    }


    @Override
    public int hashCode() {
//        return Objects.hash(key, hasChecked, selfCycle, pcMap, varMap, nextNodeList);
        return Objects.hash(toString());
    }
}
