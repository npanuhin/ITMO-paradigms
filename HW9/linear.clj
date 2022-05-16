; WARNING!!! VERY BAD!!!

(defn check-vector? [elements]
  (and (every? vector? elements)
       (apply == (map count elements))))

(defn check-num-vector? [elements]
  (and (vector? elements)
       (every? number? elements)))

(defn check-num-matrix? [elements]
  (and (check-vector? elements)
       (every? check-num-vector? elements)))

(defn check-matrix? [elements]
  (and (every? vector? elements)
       (every? check-num-matrix? elements)
       (apply == (map count elements))))

(defn element-op [f checkFunc]
  (fn [& elements]
    {:pre [(checkFunc elements)]}
    (apply mapv f elements)))

; :NOTE: Упростить
(def v+ (element-op + check-num-matrix?))
(def v- (element-op - check-num-matrix?))
(def v* (element-op * check-num-matrix?))
(def vd (element-op / check-num-matrix?))

(defn scalar [& vectors]
  {:pre [(check-num-matrix? vectors)]}
  (apply + (apply v* vectors)))

(defn vect-bin [v1 v2]
  (letfn [(cross [a b]
            (- (* (nth v1 a) (nth v2 b)) (* (nth v1 b) (nth v2 a))))]
    (vector (cross 1 2) (cross 2 0) (cross 0 1))))

(defn vect [& vectors]
  {:pre [(and
           (every? check-num-vector? vectors)
           (apply == 3 (map count vectors)))]}
  (reduce vect-bin vectors))

(defn v*s [v & scalars]
  {:pre [(and (check-num-vector? v) (every? number? scalars))]}
  (let [mul (apply * scalars)]
    (mapv #(* % mul) v)))

; :NOTE: 2x2 2x3
(def m+ (element-op v+ check-matrix?))
(def m- (element-op v- check-matrix?))
(def m* (element-op v* check-matrix?))
(def md (element-op vd check-matrix?))

(defn m*s [matrix & scalars]
  {:pre [(and (check-num-matrix? matrix)
              (every? number? scalars))]}
  (let [mul (apply * scalars)]
    (mapv #(v*s % mul) matrix)))

(defn m*v [matrix vector]
  {:pre [(and (check-num-matrix? matrix)
              (check-num-vector? vector)
              (== (count (first matrix)) (count vector)))]}
  (mapv #(apply + (v* % vector)) matrix))

(defn transpose [matrix]
  {:pre [(check-num-matrix? matrix)]}
  (apply mapv vector matrix))

(defn m*m2 [m1 m2]
  {:pre [(== (count (first m1)) (count m2))]}
  (let [m2 (transpose m2)]
    (transpose (map #(m*v m1 %) m2))))

(defn m*m [& matrixes]
  {:pre [(every? check-num-matrix? matrixes)]}
  (reduce m*m2 matrixes))

(defn shape [tensor]
  (cond
    (number? tensor) ()
    (vector? tensor) (let [sh (shape (first tensor))]
                       (if (apply = sh (map shape (rest tensor)))
                         (cons (count tensor) sh)))))

(defn check-tensor? [elements]
  (not (nil? (shape elements))))

; :NOTE: reduce
(defn copy-tensor [sh copy-end element]
  (letfn [(rep [f] (map f (repeat (first sh) element)))]
    (cond
      (> (count sh) (+ copy-end 1)) (rep #(copy-tensor (rest sh) copy-end %))
      (empty? sh) element
      :else (rep #(identity %)))))

; :NOTE: Упростить
(defn broadcasting? [l1 l2]
  (let [len1 (count l1)
        len2 (count l2)]
    (cond
      (or (== 0 len2) (== 0 len1)) true
      (== (last l1) (last l2)) (broadcasting? (butlast l1) (butlast l2))
      :else false)))

(defn broadcast [t1 t2]
  {:pre [(broadcasting? (shape t1) (shape t2))]}
  (let [l1 (shape t1)
        l2 (shape t2)
        len1 (count l1)
        len2 (count l2)]
    (cond
      (== len1 len2) [t1 t2]
      (> len1 len2) [t1 (copy-tensor l1 len2 t2)]
      :else (vector (copy-tensor l2 len1 t1) t2))))

(defn tensor-op [f unary]
  (letfn [(cnt [& elements]
            (if (every? number? elements)
              (apply f elements)
              (apply mapv cnt elements)))]
    (fn [& elements]
      {:pre [(every? check-tensor? elements)]}
; :NOTE: Арность
      (if (== 1 (count elements))
         (reduce #(apply cnt (broadcast %1 %2)) unary elements)
         (reduce #(apply cnt (broadcast %1 %2)) elements)))))

(def hb+ (tensor-op + 0))
(def hb- (tensor-op - 0))
(def hb* (tensor-op * 1))
(def hbd (tensor-op / 1))
