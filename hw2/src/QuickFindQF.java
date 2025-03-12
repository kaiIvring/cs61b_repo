public class QuickFindQF {
    private int[] id;

    public QuickFindQF(int N) {
        id = new int[N];
        for (int i = 0; i < N; i++) {
           id[i] = i;
        }
    }

    public boolean isConnected(int p, int q) {
        return id[p] == id[q];
    }

    public void connect(int p, int q) {
        int pid = id[p];
        int qid = id[q];

        for (int i = 0; i < id.length; i++) {
            if (id[i] == pid) {
                id[i] = qid;
            }
        }
    }
}
