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

(load-file "proto.clj")

(defn diff [expr target]
  ((proto-get expr :diff) expr target))
(defn toString [expr]
  ((proto-get expr :toString) expr))
(defn evaluate [expr args]
  ((proto-get expr :evaluate) expr args))

(def operands (fn [this] (proto-get this :operands)))

(defn AbstractOperationPrototype [f sign f_diff] {
  :diff f_diff
  :toString (fn [this] (str "(" sign " " (clojure.string/join " " (mapv toString (this :operands))) ")"))
  :evaluate (fn [this args] (apply f (mapv (fn [x] (evaluate x args)) (this :operands))))
  })

(defn AbstractOperation [f sign f_diff]
  (let [this {
    :prototype (AbstractOperationPrototype f sign f_diff)
    }]
    (fn [& args] (assoc this :operands args))))

(defn Constant [x] {
  :diff     (fn [this target] (Constant 0))
  :evaluate (fn [this args] x)
  :toString (fn [& args] (format "%.01f" (double x)))
  })

(defn Variable [x] {
  :diff     (fn [this target] (if (identical? target x) (Constant 1) (Constant 0)))
  :evaluate (fn [this args] (get args x))
  :toString (fn [& args] (str x))
  })

(def Add (AbstractOperation + "+" (fn [this target]
  (apply Add (map (fn [x] (diff x target)) (operands this))))))

(def Subtract (AbstractOperation - "-" (fn [this target]
  (apply Subtract (map (fn [x] (diff x target)) (operands this))))))

(def Multiply (AbstractOperation * "*" (fn [this target]
  (apply Add (mapv
    (fn [x] (apply Multiply (mapv (fn [y] (if (identical? x y) (diff x target) y)) (operands this))))
    (operands this))))))

(def Negate (AbstractOperation - "negate" (fn [this target]
  (let [x (first (operands this))]
    (Negate (diff x target))))))

(defn single-elem? [s] (and (seq s) (empty? (rest s))))
(def Divide (AbstractOperation divide-imp "/" (fn [this target]
  (if (single-elem? (operands this))
    (let [x (first (operands this))]
      (Divide (Negate (diff x target)) (Multiply x x)))

    (let [args (operands this)]
      (apply Subtract (for [i (range (count args))]
        (Divide
          (Multiply (first args) (diff (nth args i) target))
          (Multiply (nth args i) (apply Multiply (rest args)))))))))))

(def Exp (AbstractOperation (fn [x] (Math/exp x)) nil (fn [this target]
  (let [x (first (operands this))]
    (Multiply (diff x target) (Exp x))))))

(def Sumexp (AbstractOperation sumexp-imp "sumexp" (fn [this target]
  (let [args (operands this)]
    (apply Add (for [i (range (count args))]
      (diff (Exp (nth args i)) target)))))))

(def Softmax (AbstractOperation softmax-imp "softmax" (fn [this target]
  (let [args (operands this)]
    (diff (Divide (Exp (first args)) (apply Sumexp args)) target)))))

(def OperationObjects {
  '+       Add
  '-       Subtract
  '*       Multiply
  '/       Divide
  'negate  Negate
  'sumexp  Sumexp
  'softmax Softmax
  })

(defn parseObject [expr] (parse OperationObjects Constant Variable (read-string expr)))
