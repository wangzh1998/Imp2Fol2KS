import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;

public class Transition2Relation {
    int processCount = 1;
    LinkedList<KSNode> queue = new LinkedList();
    KSStructure ksStructure = new KSStructure(true);
    GraphViz gViz=new GraphViz("D:\\Users\\lenovo\\Desktop\\eee", "D:\\LenovoQMDownload\\SoftMgr\\Graphviz\\bin\\dot.exe");

    // 创建转换关系时需要知道进程数
    public Transition2Relation(int processCount){
        this.processCount = processCount;
    }

    public static void main(String[] args) {
        Transition2Relation transition2Relation = new Transition2Relation(2);
        transition2Relation.transition2Relation();
    }

    void transition2Relation(){
        gViz.start_graph();

        // 将ks中的s0状态加入队列
        for (int i = 0; i < ksStructure.initialStateList.size(); i++) {
            queue.add(ksStructure.initialStateList.get(i));
        }
        // 遍历队列中的状态，计算下一个状态
        while (!queue.isEmpty()){
            KSNode curNode = queue.poll();
            curNode.setHasChecked(true);

            ArrayList<KSNode> newNodeList = new ArrayList<>();

            if (curNode.pcMap.containsKey("pc")){
                // 含有pc，说明不是并发状态，（同时认为不存在var值的变动，即认为expr为空）
                // 遍历ks中每个Transition
                for (Transition transiton : ksStructure.transitionList) {
                    // 先检查pc是否对应    todo 需要确定map的equals方法，是否是对所有的key value去做具体值的对比
                    if (curNode.pcMap.equals(transiton.pcMap) && isValidCondition(transiton.condition, curNode)){
                        KSNode newNode = new KSNode();
                        newNodeList.add(newNode);
                        newNode.pcMap = transiton.nextPcMap;
                        break;
                    } // else do nothing
                }
            } else {
                // 不含pc，说明程序处理并发状态 每个进程处理一次对应的pci的转换 （还要处理expr）
                // 遍历每个进程
                for (int processId = 0; processId < processCount; processId++) {

                    // 遍历ks中每个Transition
                    for (Transition transiton : ksStructure.transitionList) {

                        // 先检查pc是否对应 并发状态的transition不含pc，且只含一个pci
                        String pci = "pc"+processId;
                        if (transiton.pcMap.containsKey(pci) && transiton.pcMap.get(pci).equals(curNode.pcMap.get(pci))  && isValidCondition(transiton.condition, curNode)) {
                            String transCurPcValue = transiton.pcMap.get(pci);
                            String transNextPcValue = transiton.nextPcMap.get(pci);
                            if (!transCurPcValue.equals(transNextPcValue)){
                                KSNode newNode = new KSNode();
                                newNodeList.add(newNode);
                                newNode.pcMap = transiton.nextPcMap;
                                updateVars(transiton, curNode, newNode);
                            } else { // 如果前后的pc相同，则认为是同一个状态（这种情况应该也不存在说var不同的状况吧?）
                                curNode.setSelfCycle(true);
                            }
                            break;
                        } // else do nothing
                    }
                }
            }

            if (newNodeList.size() > 0){
                for (int i = 0; i < newNodeList.size(); i++) {
                    KSNode newNode = newNodeList.get(i);
                    if (!curNode.equals(newNode)){
                        curNode.nextNodeList.add(newNode);
                        gViz.addln(curNode.getKey() + "->" + newNode.getKey() + ";");
                        gViz.addLabel(newNode.getKey(), newNode.toString());
                    } else {
                        curNode.setSelfCycle(true);
                        gViz.addln(curNode.getKey() + "->" + curNode.getKey() + ";");
                    }
                    queue.add(newNode);
                }
            }
        }
        gViz.end_graph();
        try {
            gViz.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param condition
     * 支持的判定条件说明：
     * 1. condition为空或为TRUE，返回true
     * 2. condition为FALSE, 返回false
     * 3. 支持的判定类型为单条件判断：a == b /  a == 2 / !a 形式 （其中“==”可以替换为 ！= , <=） (a指代V中变量，2指代D中的常数 !后的a不能为表达式，只能是值或者数字，所有不为0的数字都认为是true)
     * 4. 1111 要求空格规则如上示例
     * @return
     */
    boolean isValidCondition(String condition, KSNode curNode){
        boolean isValid = false;
        if (condition == null || condition.length() == 0 ||condition.equals(KSStructure.TRUE)){
            isValid = true;
        } else if (condition.equals(KSStructure.FALSE)){
            isValid = false;
        } else if (condition.length() == 1){ //单个变量
            if (curNode.varMap.get(condition) != 0) isValid = true;
            else isValid = false;
        } else {
            // todo 解析condition
            if (condition.contains(KSStructure.EQUAL) || condition.contains(KSStructure.NOT_EQUAL) || condition.contains(KSStructure.LESS_EQUAL)){
//                StringTokenizer stringTokenizer = new StringTokenizer("==|!=|<=");
                // 1111 检查这个分界是否有问题
                StringTokenizer stringTokenizer = new StringTokenizer(" ");
                if (stringTokenizer.countTokens() == 3){
                    String left = stringTokenizer.nextToken();
                    String op = stringTokenizer.nextToken();
                    String right = stringTokenizer.nextToken();
                    int leftVal = curNode.varMap.get(left);
                    int rightVal;
                    if (curNode.varMap.containsKey(right)){ // 右边为变量
                        rightVal = curNode.varMap.get(right);
                    } else { //右边为值
                        rightVal = Integer.valueOf(right);
                    }
                    switch (op) {
                        case KSStructure.EQUAL:
                            isValid = leftVal == rightVal;
                            break;
                        case KSStructure.LESS_EQUAL:
                            isValid = leftVal <= rightVal;
                            break;
                        case KSStructure.NOT_EQUAL:
                            isValid = leftVal != rightVal;
                            break;
                    }
                }
            } else if (condition.contains(KSStructure.NOT)){
                String var = condition.substring(2, condition.length()-1);
                isValid = !isValidCondition(var, curNode);
            }
        }
        return isValid;
    }

    /**
     * @param transition
     * @param curNode
     * @param newNode
     * 解析形式：
     * 1. skip 跳过
     * 2. a = b + c (+可以换成 - *， b和c可以换成常数, 空格如例)\
     * 3. 1111? same(turn) 这种形式如何处理?
     */
    void updateVars(Transition transition, KSNode curNode, KSNode newNode){
        for (String expr: transition.exprList) {
            if (expr.contains(KSStructure.SKIP)){
                // do nothing
            } else {
                StringTokenizer stringTokenizer = new StringTokenizer(" ");
                if (stringTokenizer.countTokens() == 3){
                    String result = stringTokenizer.nextToken();
                    String left = stringTokenizer.nextToken();
                    String right = stringTokenizer.nextToken();
                    int leftVal, rightVal, resultVal;
                    if (curNode.varMap.containsKey(left)){
                        leftVal = curNode.varMap.get(left);
                    } else {
                        leftVal = Integer.valueOf(left);
                    }
                    if (curNode.varMap.containsKey(right)){ // 右边为变量
                        rightVal = curNode.varMap.get(right);
                    } else { //右边为值
                        rightVal = Integer.valueOf(right);
                    }
                    if (expr.contains(KSStructure.ADD)){
                        resultVal = leftVal + rightVal;
                    } else if (expr.contains(KSStructure.SUB)){
                        resultVal = leftVal - rightVal;
                    } else if (expr.contains(KSStructure.MUL)){
                        resultVal = leftVal * rightVal;
                    } else {
                        resultVal = -1;
                    }
                    newNode.varMap.put(result, resultVal);
                }
            }
        }
    }
}



