package by.it.group410972.rak.lesson13;

import by.it.HomeWork;
import org.junit.Test;

@SuppressWarnings("NewClassNamingConvention")
public class Test_Part2_Lesson13 extends HomeWork {

    @Test
    public void testGraphA() {
        run("0 -> 1", true).include("0 1");
        run("0 -> 1, 1 -> 2", true).include("0 1 2");
        run("0 -> 2, 1 -> 2, 0 -> 1", true).include("0 1 2");
        run("0 -> 2, 1 -> 3, 2 -> 3, 0 -> 1", true).include("0 1 2 3");
        run("1 -> 3, 2 -> 3, 2 -> 3, 0 -> 1, 0 -> 2", true).include("0 1 2 3");
        run("0 -> 1, 0 -> 2, 0 -> 2, 1 -> 3, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        run("A -> B, A -> C, B -> D, C -> D", true).include("A B C D");
        run("A -> B, A -> C, B -> D, C -> D, A -> D", true).include("A B C D");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 4, 3 -> 5, 4 -> 5", true).include("0 1 2 3 4 5");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4, 4 -> 5", true).include("0 1 2 3 4 5");
        run("A -> B, B -> C, A -> D, D -> C", true).include("A B D C");
        run("A -> B, B -> C, C -> D, A -> E, E -> D", true).include("A B C E D");
        run("0 -> 2, 1 -> 2, 2 -> 3, 1 -> 3, 0 -> 1, 3 -> 4", true).include("0 1 2 3 4");
        run("0 -> 1, 1 -> 2, 2 -> 3, 0 -> 2, 1 -> 3, 3 -> 4", true).include("0 1 2 3 4");
        run("A -> B, A -> C, B -> D, C -> E, D -> F, E -> F", true).include("A B C D E F");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4, 1 -> 4, 0 -> 3", true).include("0 1 2 3 4");
        run("A -> B, B -> C, C -> D, A -> E, E -> F, F -> G, D -> H, G -> H", true).include("A B C D E F G H");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4, 4 -> 5, 2 -> 5", true).include("0 1 2 3 4 5");

    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4", true).include("no").exclude("yes");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0, 2 -> 3", true).include("yes").exclude("no");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 0", true).include("yes").exclude("no");

    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        run("A->B, B->C, C->A, C->D, D->E, E->F, F->D, G->H", true)
                .include("G\nH\nABC\nDEF");
        run("X->Y, Y->Z, Z->X, X->W, W->V, V->U", true)
                .include("XYZ\nW\nV\nU");
        run("M->N, N->O, O->P, P->M, Q->R, R->S, S->Q, T->U", true)
                .include("T\nU\nQRS\nMNOP");
        run("A->B, B->C, C->D, D->E, E->F", true)
                .include("A\nB\nC\nD\nE\nF");
        run("K->L, L->M, M->K, N->O, O->N, P->Q, Q->R, R->P", true)
                .include("NO\nKLM\nPQR");
        run("S->T, T->U, U->V, V->S, W->X, X->Y, Y->Z, Z->W, A->B", true)
                .include("WXYZ\nSTUV\nA\nB");
    }



}