package ID3;

public class Node
{
    public Node parent;
    public Node[] children;
    public boolean classification;

    private char DNA;
    private int pos;
    private String classStr;

    public char getDNA() { return DNA; }
    public int getPos()
    {
        if(pos == -1) return parent.pos;
        else return pos;
    }
    public String getClassStr() { if(classification) return classStr; else return "";}
    public void setClassification(String str) { classStr = str; }
    public void setPos(int posi) { pos = posi; }
    public void setParent(Node par) { parent = par; }

    public Node()
    {

        children = new Node[1];
        parent = null;
        DNA = ' ';
        pos = 0;
        classification = false;
    }
    public Node(Node[] child, Node par, char dna, int position)
    {
        children = child;
        parent = par;
        DNA = dna;
        pos = position;
        classification = false;
    }
    public void print()
    {
        print("", true);
    }
    private void print(String prefix, boolean isTail)
    {
        if(pos == -1) System.out.println(prefix + (isTail ? "└── " : "├── ") + DNA);
        else if (!classification) System.out.println(prefix + (isTail ? "└── " : "├── ") + pos);
        else System.out.println(prefix + (isTail ? "└── " : "├── ") + classStr);
        int cLength;
        if(children[0] == null) cLength = 0;
        else cLength = children.length;

        for (int i = 0; i < cLength - 1; i++)
        {
            children[i].print(prefix + (isTail ? "    " : "│   "), false);
        }
        if (cLength > 0)
        {
            children[cLength - 1].print(prefix + (isTail ? "    " : "│   "), true);
        }
    }
}
