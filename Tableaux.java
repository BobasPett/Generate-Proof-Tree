
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;

public class Tableaux {





   


    private final StringBuilder table = new StringBuilder();
    public static String HTML_START = "<html>";
    public static String HTML_END = "</html>";
    public static String TABLE_START_BORDER = "<table border=\"1\">";
    public static String TABLE_START = "<table>";
    public static String TABLE_END = "</table>";
    public static String HEADER_START = "<th>";
    public static String HEADER_END = "</th>";
    public static String ROW_START = "<tr>";
    public static String ROW_END = "</tr>";
    public static String COLUMN_START = "<td>";
    public static String COLUMN_END = "</td>";






    public Node root;
    int totalNodes=0;
    ArrayList<Node> listOfNodes=new ArrayList<>();


    int canvasWidth=20;
    int canvasHeight=20;
   



    public Tableaux() {
        root = null;
    }
    public class Node {

        private boolean contradiction;
        private LinkedList<String> formulas = new LinkedList<>();
        private LinkedList<String> tableFormulas = new LinkedList<>();
        private LinkedList<String> reasons = new LinkedList<>();

        private int x;
        private int y;

        private Node left;
        private Node right;


        public void setData(LinkedList<String> formulas) {
            this.formulas = formulas;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }


        public Node(LinkedList<String> formulas, boolean contradiction) {
            this.formulas = formulas;
            this.tableFormulas=formulas;
            this.contradiction = contradiction;
        }
    }







    /**
     * Make table containing Tableaux
     * @param w left and right distance
     * @param h up and down distance
     */
    public void makeHTMLTable(int w, int h) {
        StringBuilder sb = new StringBuilder();
        table.append(HTML_START);
        table.append(TABLE_START);


        for (int i = 0; i < h; i++) {
            sb.append(ROW_START);
            for (int j = 0; j < w; j++) {
                sb.append(COLUMN_START);

                if(listOfNodes.size()>0) {
                    Node curr = listOfNodes.get(0);
                    //check if position is one of the ordered pairs
                    if (curr.x == j && curr.y == i) {



                        sb.append("<table border=\"1\"width=\"30\" height=\"30\">");
                        for (int k = 0; k < curr.formulas.size(); k++) {

                            sb.append(ROW_START);
                            sb.append(COLUMN_START);

                            sb.append(curr.formulas.get(k));

                            sb.append(COLUMN_END);
                            sb.append(ROW_END);

                        }
                        sb.append(TABLE_END);

                        listOfNodes.remove(0);
                    }
                }


                sb.append(COLUMN_END);
            }
            sb.append(ROW_END);
        }

        table.append(sb);
        table.append(TABLE_END);
        table.append(HTML_END);

    }

    /**
     * @param s string you want to change
     * @param replace every occurrence of this character is replaced w/ "with"
     * @param with the string that replaces "replace"
     * @return string with replacements
     */
    public static String replace(String s, char replace, String with){
        int i=0;
        while(i != s.length() ){
            if(s.charAt(i)==replace) {
                String stuffAfter = s.substring(i + 1, s.length());
                String stuffBefore = s.substring(0,i);
                s =  stuffBefore + with + stuffAfter;//insert string for the 'replace' character
                i = i + with.length();
            }
            else i++;
        }

        return s;
    }

    /**
     * use this to change logic operators to html syntax
     */
    public void replaceHTML(){
        for(int i=0;i<listOfNodes.size();i++){
            for(int j=0;j<listOfNodes.get(i).formulas.size();j++){
                String currFormula=listOfNodes.get(i).formulas.get(j);

                currFormula=replace(currFormula, '&',"&and;");
                currFormula=replace(currFormula, 'V', "&or;");
                currFormula=replace(currFormula, '~', "&not;");
                currFormula=replace(currFormula, '>', "&sup;");
                currFormula=replace(currFormula, '<', "&hArr;");

                listOfNodes.get(i).formulas.set(j,currFormula);
            }

        }
    }




    public void printGraph(int hBetween){inorderTraversal(root,0,hBetween);}
    /**
     * set (x,y) coordinates for each node in tree
     * @param node
     * @param depth 
     * @param hbetween height between each horizontal layer 
     */
    public void inorderTraversal(Node node,int depth,int hbetween) {
        if (node != null) {
            inorderTraversal(node.left, depth + hbetween , hbetween);
            node.x = totalNodes++;
            node.y=depth;
            inorderTraversal(node.right, depth + hbetween, hbetween);
        }
    }


    /**
     * Put Nodes in listOfNodes
     */
    public void traverseTree() {
        java.util.Queue<Node> queue = new LinkedList<>();
        if (root == null) return;
        Node currentNode = root;
        queue.add(currentNode);

        int countCurrent = 1;
        int countNext = 0;

        while (!queue.isEmpty()) {
            currentNode = queue.poll();
            countCurrent--;

            listOfNodes.add(currentNode);

            if (currentNode.left != null)
                queue.add(currentNode.left);

            if (currentNode.right != null)
                queue.add(currentNode.right);

            countNext = countNext + 2;
            if (countCurrent == 0) {
                countCurrent = countNext;
                countNext = 0;
            }
        }

    }




    public void insert(LinkedList<String> data) {root = insert(root, data);}
    private Node insert(Node node, LinkedList<String> data) {
        if (node == null)
            node = new Node(data, false);
        else {
            if (node.getRight() == null)
                node.right = insert(node.right, data);
            else
                node.left = insert(node.left, data);
        }
        return node;
    }

    /**
     * @param str string w/ white spaces
     * @return string w/out white spaces
     */
    public String removeWhiteSpaces(String str) {
        char[] strArray = str.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strArray.length; i++) {
            if ((strArray[i] != ' ') && (strArray[i] != '\t')) {
                sb.append(strArray[i]);
            }
        }
        System.out.println(sb);
        return sb.toString();
    }



    /**
     * @param node object has formulas which are checked for contradiction
     * @return true if both A and ~A is found in list of statements
     */
    public boolean isContradiction(Node node) {
        if (node.formulas.size() >= 2) {//contradiction defined by 2 elements ~A and A
            if (node.contradiction) return true;
            if (node.formulas.size() > 2) {
                int length = node.formulas.size();
                for (int i = 0; i < length; i++) {
                    String temp = node.formulas.get(i);
                    node.formulas.remove(temp);
                    if(isContradiction(node)) return true;
                    node.formulas.add(temp);
                }
            }

            //if (A,~A) or (~A,A)
            if ((node.formulas.get(0).equals("~" + node.formulas.get(1))) || (node.formulas.get(1).equals("~" + node.formulas.get(0)))) {
                System.out.println("CONTRADICTION: ");
                System.out.println(node.formulas.get(0) + " AND " + node.formulas.get(1));
                node.contradiction = true;
                return true;
            }
        }
        else {
            System.out.println("size of list " + node.formulas + " < 2 no contradiction can be established");
            return false;
        }
        return false;
    }


    /**
     * Use this in parseTableaux() to see if any logic equivalences can be used
     * on a group of statements
     *
     * @param formulas list of string formulas for some node
     * @return true if there is an operator other than V in formulas
     */
    public boolean hasOtherOp(LinkedList<String> formulas) {
        for (String f : formulas) {

            //if f is an atomic statement (~A , A) then it has no operator so skip it
            if (f.length() <= 2) continue;



            //if f is ~(...)
            if ((f.charAt(0) == '~') && (f.charAt(1)=='(')) {

                //check whether formula is like ~(PVQ) or ~(PVQ)&(RVS)

                int rightParen = 1;//left paren is at position 1
                int count = 1;
                while (count > 0) {
                    rightParen++;
                    char c = f.charAt(rightParen);
                    if (c == '(') {
                        count++;
                    } else if (c == ')') {
                        count--;
                    }
                }

                //check if leftmost parenthesis pairs with a parenthesis
                // at the end of the formula
                if (rightParen == (f.length() - 1)) return true;

            }


            String[] temparr = parseFormula(f);
            String op = temparr[1];//get main operator for each formula
            if (!op.equals("V") && (f.length() > 2)) {
                System.out.println(op + " in formula " + f + " doesnt = V and length > 2");
                return true;
            }
        }
        return false;
    }


    /**
     * @param formula string that has left and right parenthesis
     * @param index index of left parenthesis
     * @return index of right parenthesis
     */
    public int getMatch(String formula, int index){
        int rightParen = index;
        int count = 1;
        while (count > 0) {
            rightParen++;
            char c = formula.charAt(rightParen);
            if (c == '(') {
                count++;
            } else if (c == ')') {
                count--;
            }
        }

        return rightParen;
    }

    /**
     * @param formula string to test
     * @return true if parentheses are validly placed
     */
    public boolean parenMatch(String formula) {
        Stack parenStack = new Stack();
        for (int i = 0; i < formula.length(); i++) {
            char token = formula.charAt(i);

            if (token == '(') {
                parenStack.push(token);
            }
            else if (token == ')') {
                if (parenStack.isEmpty()) {
                    return false;
                }
                parenStack.pop();
            }
        }

        if (parenStack.isEmpty()) {
            return true;
        }

        return false;
    }

    /**
     * 
     * @param s string w/ parentheses
     * @return the string without unnecessary parenthesis
     */
    public String removeParenthesis(String s) {

        String result = "";

        char[] ops = {'<', '>', 'V', '&'};
        ArrayList<Character> operators = new ArrayList<>();
        for (char c : ops)
            operators.add(c);

        ArrayList<Character> copy = new ArrayList<>();
        for (int i = 0; i < s.length(); i++)
            copy.add(s.charAt(i));


        ArrayList<ArrayList<Integer>> parenthesisPairs = getIndices(s);
        ArrayList<Integer> redundent = new ArrayList<>();

        for (ArrayList<Integer> pair : parenthesisPairs) {

            int leftIndex = pair.get(0);
            int rightIndex = pair.get(1);

            Character leftOp = ' ';//character left of left paren
            Character rightOp = ' ';//character right of right paren

            //Handle IndexOutOfBoundsException
            //if smaller than smallest index
            if ((leftIndex - 1) > 0) {
                leftOp = s.charAt(leftIndex - 1);
            }

            //if larger than largest index
            if ((rightIndex + 1) < (s.length() - 1)) {
                rightOp = s.charAt(rightIndex + 1);
            }


            //if the character to the left and to the right isnt
            // an op then the parentheses are redundent
            if (!operators.contains(leftOp) && !operators.contains(rightOp)) {
                redundent.add(leftIndex);
                redundent.add(rightIndex);
            }
            else {
                //look in between the focus parenthesis pair and find the operator w/ smallest priority
                int tmpLeft = leftIndex;
                Character smallestPriority = ' ';


                while (tmpLeft != rightIndex) {
                    tmpLeft++;
                    //if next character is an op
                    //if operator has smaller priority than smallest priority
                    if (operators.contains(s.charAt(tmpLeft))) {
                        if (operators.indexOf(s.charAt(tmpLeft)) < smallestPriority) {
                            smallestPriority = s.charAt(tmpLeft);
                        }
                    }
                }

                //if smallestPriority is larger than leftOp AND if smallestPriority is larger than rightOp
                //then redundant
                if ((s.indexOf(smallestPriority) > s.indexOf(leftOp)) && (s.indexOf(smallestPriority) > s.indexOf(rightOp))) {
                    redundent.add(leftIndex);
                    redundent.add(rightIndex);
                }

            }
        }


        Collections.sort(redundent, Collections.reverseOrder());
        for (int i : redundent)
            copy.remove(i);

        for (char c : copy)
            result = result + c;


        return result;
    }


    /**
     * @param formula "A OP B"
     * @return [A, OP, B]
     */
    public String[] parseFormula(String formula) {
        //make sure parentheses match
        if (!parenMatch(formula)) {
            System.out.println("this formula:  '" + formula + "' doesnt work");
            String[] result = {};
            return result;
        }


        //variables
        ArrayList<Character> operators = new ArrayList<>();
        operators.add('<');
        operators.add('>');
        operators.add('V');
        operators.add('&');
        String[] output = new String[3];
        int countl =0;
        int countr =0;
        int countOps =0;
        int indexOfOp =0;

        //before removing redundant parentheses check for formulas like ~(A&B) and atomic statements

        //if formula = A or ~A then return
        if(formula.length()<3){
            output[0] = "";//A
            output[1] = "";//OP
            output[2] = "";//B

            return output;
        }


        //if formula = ~(A&B)
        if ( (formula.charAt(0)=='~') && (formula.charAt(1)=='(') ){
            //left paren is at position 1
            int rightParen = 1;
            int count = 1;
            while (count > 0) {
                rightParen++;
                char c = formula.charAt(rightParen);
                if (c == '(') {
                    count++;
                } else if (c == ')') {
                    count--;
                }
            }

            // if leftmost parenthesis pairs with a parenthesis
            // at the end of the formula then ~ is the operator
            if (rightParen == (formula.length() - 1)) {
                output[0] = "";//A
                output[1] = "" + formula.charAt(0);//OP
                output[2] = "";//B
                return output;
            }
        }
        formula=removeParenthesis(formula);




        //Handle some exceptions:
        //if formula = AVB , if formula = ~AVB
        for (int i = 0; i < formula.length(); i++) {
            if (operators.contains(formula.charAt(i)))
                countOps++;
        }

        if (countOps == 1) {
            if (formula.charAt(0) == '~')
                indexOfOp = 2;
            else
                indexOfOp = 1;

            output[0] = formula.substring(0, indexOfOp);//A
            output[1] = "" + formula.charAt(indexOfOp);//OP
            output[2] = formula.substring(indexOfOp + 1, formula.length());//B
            return output;

        }


        for (int i = 0; i < formula.length(); i++) {
            if (formula.charAt(i) == '(')
                countl++;
            if (formula.charAt(i) == ')')
                countr++;
            if ((countl == countr) && countl != 0) {
                indexOfOp = i;
                break;
            }
        }

        if ((countl != countr) || countl == 0) {
            System.out.println("this formula:  '" + formula + "' doesnt work");
            return output;
        }


        output[0] = formula.substring(0, indexOfOp + 1);//A
        output[1] = "" + formula.charAt(indexOfOp + 1);//OP
        output[2] = formula.substring(indexOfOp + 2, formula.length());//B

        return output;
    }


    /**
     * @return indices of each parenthesis pair {(x1,y1),(x2,y2)...}
     */
    public ArrayList<ArrayList<Integer>> getIndices(String s) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                int rightParen = i;
                int count = 1;
                while (count > 0) {
                    rightParen++;
                    char c = s.charAt(rightParen);
                    if (c == '(') {
                        count++;
                    } else if (c == ')') {
                        count--;
                    }
                }
                ArrayList<Integer> indices = new ArrayList<>();
                indices.add(i);
                indices.add(rightParen);
                result.add(indices);
            }
        }
        return result;
    }



    public void resolveNegations(Node node) {

        for(int j=0;j<node.formulas.size();j++) {

            //if current formula has ~ on far left
            if (node.formulas.get(j).charAt(0) == '~') {

                int countNots = 0;
                while (node.formulas.get(j).charAt(countNots) == '~') {
                    countNots++;
                }

                //if even # of negations then remove all of them
                if (countNots % 2 == 0)
                    node.formulas.set(j, node.formulas.get(j).substring(countNots));
                    //if odd # of negations then leave 1
                else
                    node.formulas.set(j, node.formulas.get(j).substring(countNots - 1));
            }
        }

    }




    /**
     * Input: group of logic formulas and conclusion
     * Output: Tableaux proof
     *
     * Base case: formula list contains contradiction
     *
     * f(node){
     *
     *     if  formula list contains contradiction
     *        return true
     *
     *     if (f(node1) AND f(node2))
     *        return true
     *
     * }
     *
     * @param node root of proof tree
     * @return true if there is a contradiction
     */
    public boolean parseTableaux(Node node) {

        if (isContradiction(node))
            return true;


        //while there is a formula in list whose main op is either ~,&,>,< break it up
        while (hasOtherOp(node.formulas)) {

            

            if(isContradiction(node))
                return true;

            //for each formula in the list
            for (int i = 0; i < node.formulas.size(); i++) {
                node.formulas.set(i, removeWhiteSpaces(node.formulas.get(i)));
                LinkedList<String> f = node.formulas;



                //if (formula = either ~A, A) then skip it
                if ((f.get(i).length() <= 2)) continue;


                String[] parsedFormula = parseFormula(f.get(i));
                String A = parsedFormula[0];
                String op = parsedFormula[1];
                String B = parsedFormula[2];

                //if (formula op = V) then skip it
                if (op.equals("V")) continue;


                //NEGATIONS
                if(f.get(i).charAt(0)=='~'){

                    //remove double negations
                    int countNots = 0;
                    while (f.get(i).charAt(countNots) == '~') {
                        countNots++;
                    }

                    //if even # of negations then remove all of them
                    if (countNots % 2 == 0)
                        f.set(i, f.get(i).substring(countNots));
                        //if odd # of negations then leave 1
                    else
                        f.set(i, f.get(i).substring(countNots - 1));



                    //if the main op in formula is ~
                    if (getMatch(f.get(i),1)==(f.get(i).length()-1)) {
                        //EX. ~(A&B) then negatedFormula = A&B
                        String negatedFormula = f.get(i).substring(2, f.get(i).length() - 1);
                        parsedFormula = parseFormula(negatedFormula);

                        A = parsedFormula[0];
                        op = parsedFormula[1];
                        B = parsedFormula[2];

                        f.remove(i);

                        //AND: ~(A&B) <-> ~AV~B
                        if (op.equals("&")) {
                            //~AV~B
                            f.add("~" + A + "V" + "~" + B);
                        }
                        //OR: ~(AVB) <-> (~A&~B)
                        else if (op.equals("V")) {
                            //~A&~B
                            f.add("~" + A);
                            f.add("~" + B);
                        }
                        //CONDITIONAL: ~(A>B) <-> A&~B
                        else if (op.equals(">")) {
                            //~B&A
                            f.add(A);
                            f.add("~" + B);
                        }
                        //EQUIVALENCE: ~(A<B) <-> (A&~B)V(~A&B)
                        else if (op.equals("<")) {
                            //(A&~B)V(B&~A)
                            f.add("(" + A + "&" + "~" + B + ")" + "V" + "(" + B + "&" + "~" + A + ")");
                        }

                        resolveNegations(node);
                        if(isContradiction(node))
                            return true;


                    }//end if

                }//end if


                //if formula has operators other than DISJUNCTION and
                //if there are no negations ~ in front
                else if (!op.equals("V")) {
                    f.remove(i);

                    //AND: (A&B) <-> A,B
                    if (op.equals("&")) {
                        //A,B
                        f.add(A);
                        f.add(B);
                    }
                    //CONDITIONAL: A>B <-> ~AVB
                    else if (op.equals(">")) {
                        //~AVB
                        f.add("~" + A + "V" + B);

                    }
                    //EQUIVALENCE: A<B <-> (A&B)V(~A&~B)
                    else if (op.equals("<")) {
                        //(A&B)V(~A&~B)
                        f.add("(" + A + "&" + B + ")" + "V" + "(" + "~"+A + "&" + "~"+B + ")");
                    }

                    resolveNegations(node);
                    if(isContradiction(node))
                        return true;



                }//end else if

            }//end for

        }//end while


        //after you check for equivalences create left + right nodes for every DISJUNCTION
        for (int i = 0; i<node.formulas.size(); i++) {



            //if formula is a DISJUNCTION
            if (node.formulas.get(i).length()>2) {
                String[] parseformula = parseFormula(node.formulas.get(i));


                LinkedList<String> disjunct1 =new LinkedList<>();
                LinkedList<String> disjunct2 =new LinkedList<>();


                for(String s:node.formulas){
                    disjunct1.add(s);
                    disjunct2.add(s);
                }
                disjunct1.remove(i);
                disjunct2.remove(i);

                disjunct1.add(parseformula[0]);
                disjunct2.add(parseformula[2]);

                Node nodeD1 = new Node(disjunct1,false);
                Node nodeD2 = new Node(disjunct2,false);

                node.setLeft(nodeD1);
                node.setRight(nodeD2);

                if(parseTableaux(node.left) && parseTableaux(node.right))
                    return true;

            }//end if
            
        }//end for
        
        return false;
    }







//   ********************MAIN********************   //

    public static void main(String[] args) {
        Tableaux t = new Tableaux();
        LinkedList<String> formulasTest = new LinkedList<>();



        /*


        OPERATORS:

        ~ is "not"
        V is "or"
        & is "and"
        > is "implication operator"
        < is "biconditional"



        SOME PROOF TESTS:




        formulasTest.add("(~AVD)>(B>F)");
        formulasTest.add("(BVC)>(AVE)");
        formulasTest.add("AVB");
        formulasTest.add("~A");
        formulasTest.add("~(EVF)");



        formulasTest.add("P>Q");
        formulasTest.add("P");
        formulasTest.add("~Q");



        formulasTest.add("P>R");
        formulasTest.add("Q>R");
        formulasTest.add("~((PVQ)>R)");

        formulasTest.add("~(P<Q)");
        formulasTest.add("~(P<~Q)");



        */






        //formulasTest.add("premise");

        formulasTest.add("(~AVD)>(B>F)");
        formulasTest.add("(BVC)>(AVE)");
        formulasTest.add("AVB");
        formulasTest.add("~A");
        formulasTest.add("~(EVF)");

        t.insert(formulasTest);
        System.out.println("root node = " + t.root.formulas);
        t.parseTableaux(t.root);





        t.printGraph(2);
        t.traverseTree();//make list of nodes in level order
        t.replaceHTML();
        t.makeHTMLTable(20,20);




        System.out.println();
        System.out.println("********* HTML Table *********");
        System.out.println();
        System.out.println(t.table);



        

    }
}
