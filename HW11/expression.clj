(defn abstractOperation [f] (fn [& args] (fn [vars] (apply f (map #(% vars) args)))))

(def constant constantly)
(defn variable [name] (fn [vars] (vars name)))
(def add (abstractOperation +))
(def subtract (abstractOperation -))
(def multiply (abstractOperation *))
(def negate (abstractOperation -))

(defn divide-imp
  ([arg] (/ 1 (double arg)))
  ([frst & args] (/ (double frst) (reduce * args))))
(def divide (abstractOperation divide-imp))

(defn sumexp-imp [& args] (reduce + (map (fn [x] (Math/exp x)) args)))
(def sumexp (abstractOperation sumexp-imp))

(defn softmax-imp [& args] (/ (Math/exp (first args)) (apply sumexp-imp args)))
(def softmax (abstractOperation softmax-imp))

(def OperantionFunctions {
  '+       add
  '-       subtract
  '*       multiply
  '/       divide
  'negate  negate
  'sumexp  sumexp
  'softmax softmax
  })

(defn parse [operationType const-imp var-imp expr]
  (cond
    (seq? expr)    (apply (operationType (first expr)) (map #(parse operationType const-imp var-imp %) (rest expr)))
    (number? expr) (const-imp (double expr))
    (symbol? expr) (var-imp (str expr))))

(defn parseFunction [expression] (parse OperantionFunctions constant variable (read-string expression)))


; ----------------------------- HW 11 -----------------------------

(defn proto_get [this key]
  (if (contains? this key)
    (this key)
    (recur (this :prototype) key)))

(defn diff [expr target]
  ((proto_get expr :diff) expr target))
(defn toString [expr]
  ((proto_get expr :toString) expr))
(defn evaluate [expr args]
  ((proto_get expr :evaluate) expr args))

(def operands (fn [this] (proto_get this :operands)))

(def AbstractOpProto {
  :toString (fn [this]
    (str
      "("
      (this :sign)
      (if (empty? (this :operands)) (str " ") (apply str (mapv (fn [x] (str " " (toString x))) (this :operands))))
      ")"))
  :evaluate (fn [this args] (apply (this :func) (mapv (fn [x] (evaluate x args)) (this :operands))))})

(defn AbstractOp [f sign f_diff]
  (let [this {
    :prototype AbstractOpProto
    :func f
    :sign sign 
    :diff f_diff
    }]
    (fn [& args] (assoc this :operands args))))

(defn Constant [x] {
  :evaluate (fn [this args] x)
  :toString (fn [& args] (format "%.01f" (double x)))
  :diff     (fn [this target] (Constant 0))
  })

(defn Variable [x] {
  :evaluate (fn [this args] (get args x))
  :toString (fn [& args] (str x))
  :diff     (fn [this target] (if (identical? target x) (Constant 1) (Constant 0)))
  })

(def Add (AbstractOp + "+" (fn [this target]
  (apply Add (map (fn [x] (diff x target)) (operands this))))))

(def Subtract (AbstractOp - "-" (fn [this target]
  (apply Subtract (map (fn [x] (diff x target)) (operands this))))))

(def Multiply (AbstractOp * "*" (fn [this target]
  (apply Add (mapv
    (fn [x] (apply Multiply (mapv (fn [y] (if (identical? x y) (diff x target) y)) (operands this))))
    (operands this))))))

(def Negate (AbstractOp - "negate" (fn [this target]
  (let [x (first (operands this))] (Negate (diff x target))))))

(defn single-elem? [s] (and (seq s) (empty? (rest s))))
(def Divide (AbstractOp divide-imp "/" (fn [this target]
  (if (single-elem? (operands this))
    (let [x (first (operands this))] (Divide (Negate (diff x target)) (Multiply x x)))

    (let [args (operands this)]
      (apply Subtract (for [i (range (count args))]
        (Divide
          (Multiply (first args) (diff (nth args i) target))
          (Multiply (nth args i) (apply Multiply (rest args)))))))))))

(def Exp (AbstractOp (fn [x] (Math/exp x)) nil (fn [this target]
  (let [x (first (operands this))] (Multiply (diff x target) (Exp x))))))

(def Sumexp (AbstractOp sumexp-imp "sumexp" (fn [this target]
  (let [args (operands this)]
    (apply Add (for [i (range (count args))]
      (diff (Exp (nth args i)) target)))))))

(def Softmax (AbstractOp softmax-imp "softmax" (fn [this target]
  (let [args (operands this)]
    (diff (Divide (Exp (first args)) (apply Sumexp args)) target)))))

(def OperationObjects
  {'+       Add
   '-       Subtract
   '*       Multiply
   '/       Divide
   'negate  Negate
   'sumexp  Sumexp
   'softmax Softmax
  })

(defn parseObject [expr] (parse OperationObjects Constant Variable (read-string expr)))
