%% node(Key, Value, Priority, Size, Left, Right)

get_size(null, 0) :- !.  %% Размер пустого дерева
get_size(node(Key, Value, Priority, Size, Left, Right), Size).  %% Node size
%% Construct Node from vertex, left and right. Determine its size
node_gen(Key, Value, Priority, Left, Right, node(Key, Value, Priority, Size, Left, Right)) :- get_size(Left, SL),
                                                                                              get_size(Right, SR),
                                                                                              Size is SL + SR + 1.

merge(null, T2, T2) :- !.  %% nothing + Tree2 = Tree2
merge(T1, null, T1) :- !.  %% Tree1 + nothing = Tree1
%% If Tree1.priority > Tree2.priority:
merge(node(K1, V1, P1, S1, L1, R1), node(K2, V2, P2, S2, L2, R2), Result) :- P1 > P2, !,
                                                                    merge(R1, node(K2, V2, P2, S2, L2, R2), RES_R),  %% Merge (R1, Node2) -> RES_R
                                                                    node_gen(K1, V1, P1, L1, RES_R, Result).         %% Result = Node1 with R = (R, Node2)
%% else:
merge(node(K1, V1, P1, S1, L1, R1), node(K2, V2, P2, S2, L2, R2), Result) :-
                                                                    merge(node(K1, V1, P1, S1, L1, R1), L2, RES_L),  %% Merge (Node1, L2) -> RES_L
                                                                    node_gen(K2, V2, P2, RES_L, R2, Result).         %% Result = Node2 with L = (Node1, L)

split(null, _, null, null, null) :- !.  %% Split у пустого дерево -> пустота
split(node(K, V, P, S, L, R), K, L, node(K, V, P, 1, null, null), R) :- !.  %% Если X.Key == Key -> RES = (L, X, R)
split(node(K, V, P, S, L, R), Key, TL, TM, TR) :- K < Key, !,
                                                  split(R, Key, RES_TLR, TM, TR),
                                                  node_gen(K, V, P, L, RES_TLR, TL).

split(node(K, V, P, S, L, R), Key, TL, TM, TR) :- split(L, Key, TL, TM, TR_NEW),
                                                  node_gen(K, V, P, TR_NEW, R, TR).

map_put(TreeMap, Key, Value, Result) :- rand_int(2147483647, P),  %% Случайный приоритет
                                        split(TreeMap, Key, TL, TM, TR),
                                        merge(TL, node(Key, Value, P, 1, null, null), RES_TL),
                                        merge(RES_TL, TR, Result).

map_build([], null) :- !.
map_build([(K, V) | L], Result) :- map_build(L, T),  %% Построим дерево от Tail
                                   map_put(T, K, V, Result).  %% ... и положим в него (K, V)

map_get(node(K, V, P, S, L, R), K, V) :- !.  %% Нашли (Key, Value)
map_get(node(K, V, P, S, L, R), Key, Value) :- Key < K, !, map_get(L, Key, Value).  %% Идём в левое поддерево
map_get(node(K, V, P, S, L, R), Key, Value) :- map_get(R, Key, Value).  %% Идём в правое поддерево

map_remove(TreeMap, Key, Result) :- split(TreeMap, Key, TL, TM, TR), merge(TL, TR, Result).  %% Split по ключу и merge без центра

map_subMapSize(MTreeMapap, FromKey, ToKey, 0) :- FromKey >= ToKey, !.
map_subMapSize(TreeMap, FromKey, ToKey, Result) :- split(TreeMap, FromKey, TL1, TM1, TR1),
                                                   split(TR1, ToKey, TL2, TM2, TR2),
                                                   get_size(TM1, S1),
                                                   get_size(TL2, S2),
                                                   Result is S1 + S2.
