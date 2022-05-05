; WARNING!!! VERY BAD!!!

(defn check-vector? [elements]
  (and (every? vector? elements)
       (apply == (mapv count elements))))

(defn check-num-vector? [elements]
  (and (vector? elements)
       (every? number? elements)))

(defn check-num-matrix? [elements]
  (and (check-vector? elements)
       (every? check-num-vector? elements)))

(defn check-matrix? [elements]
  (and (every? vector? elements)
       (every? check-num-matrix? elements)
       (apply == (mapv count elements))))

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
           (apply == 3 (mapv count vectors)))]}
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
    (transpose (mapv #(m*v m1 %) m2))))

(defn m*m [& matrixes]
  {:pre [(every? check-num-matrix? matrixes)]}
  (reduce m*m2 matrixes))

(defn shape [tensor]
  (cond
    (number? tensor) ()
    (vector? tensor) (let [sh (shape (first tensor))]
                       (if (apply = sh (mapv shape (rest tensor)))
                         (cons (count tensor) sh)))))

(defn check-tensor? [elements]
  (not (nil? (shape elements))))

; :NOTE: reduce
(defn copy-tensor [sh copy-end element]
  (letfn [(rep [f] (mapv f (repeat (first sh) element)))]
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




; (println
;   (shape (vector (vector 5.8 2.8 0.3) (vector 8.1 8.4 5.7)))
; )
; (println
;  (shape (vector (vector (vector (vector (vector (vector 8.4 9.3 5.6) (vector 2.7 7.3 2.4)) (vector (vector 1.5 6.2 9.5) (vector 9.1 8.4 1.3)) (vector (vector 5.2 8.4 9.4) (vector 4.2 1.6 4.9)))) (vector (vector (vector (vector 4.5 8.3 5.3) (vector 6.9 7.0 4.7)) (vector (vector 9.8 9.3 4.4) (vector 9.4 2.9 5.0)) (vector (vector 3.1 3.3 0.3) (vector 5.6 3.5 7.8)))) (vector (vector (vector (vector 6.4 0.1 3.0) (vector 8.0 3.4 9.2)) (vector (vector 2.7 3.2 3.7) (vector 6.3 1.8 1.4)) (vector (vector 7.1 2.3 3.1) (vector 2.6 6.4 2.7))))) (vector (vector (vector (vector (vector 8.6 0.3 0.2) (vector 6.3 8.1 1.3)) (vector (vector 6.9 1.3 1.7) (vector 2.6 4.6 5.0)) (vector (vector 5.4 0.4 8.4) (vector 2.5 7.7 0.2)))) (vector (vector (vector (vector 0.7 7.6 4.3) (vector 7.7 6.7 0.2)) (vector (vector 4.0 4.0 0.2) (vector 2.1 5.4 4.3)) (vector (vector 4.7 7.0 1.8) (vector 0.5 4.5 1.2)))) (vector (vector (vector (vector 2.4 8.2 2.4) (vector 0.7 2.3 4.6)) (vector (vector 6.4 5.6 0.3) (vector 9.4 8.8 2.5)) (vector (vector 1.4 3.7 5.3) (vector 1.8 3.4 5.6))))) (vector (vector (vector (vector (vector 7.0 0.5 3.8) (vector 4.7 0.6 8.1)) (vector (vector 9.0 2.9 3.6) (vector 7.8 9.3 9.0)) (vector (vector 0.2 5.5 3.0) (vector 7.7 0.9 7.7)))) (vector (vector (vector (vector 3.5 9.6 6.1) (vector 6.5 6.2 3.1)) (vector (vector 0.1 4.8 0.6) (vector 2.4 1.1 7.1)) (vector (vector 5.8 2.8 9.8) (vector 0.6 6.5 5.0)))) (vector (vector (vector (vector 3.6 3.4 6.8) (vector 0.8 0.4 6.5)) (vector (vector 9.6 4.0 2.1) (vector 3.3 3.8 0.8)) (vector (vector 5.1 5.7 3.8) (vector 1.5 0.2 1.3)))))))
; )

; (println
;  (broadcast (vector (vector 5.8 2.8 0.3) (vector 8.1 8.4 5.7)) (vector (vector (vector (vector (vector (vector 8.4 9.3 5.6) (vector 2.7 7.3 2.4)) (vector (vector 1.5 6.2 9.5) (vector 9.1 8.4 1.3)) (vector (vector 5.2 8.4 9.4) (vector 4.2 1.6 4.9)))) (vector (vector (vector (vector 4.5 8.3 5.3) (vector 6.9 7.0 4.7)) (vector (vector 9.8 9.3 4.4) (vector 9.4 2.9 5.0)) (vector (vector 3.1 3.3 0.3) (vector 5.6 3.5 7.8)))) (vector (vector (vector (vector 6.4 0.1 3.0) (vector 8.0 3.4 9.2)) (vector (vector 2.7 3.2 3.7) (vector 6.3 1.8 1.4)) (vector (vector 7.1 2.3 3.1) (vector 2.6 6.4 2.7))))) (vector (vector (vector (vector (vector 8.6 0.3 0.2) (vector 6.3 8.1 1.3)) (vector (vector 6.9 1.3 1.7) (vector 2.6 4.6 5.0)) (vector (vector 5.4 0.4 8.4) (vector 2.5 7.7 0.2)))) (vector (vector (vector (vector 0.7 7.6 4.3) (vector 7.7 6.7 0.2)) (vector (vector 4.0 4.0 0.2) (vector 2.1 5.4 4.3)) (vector (vector 4.7 7.0 1.8) (vector 0.5 4.5 1.2)))) (vector (vector (vector (vector 2.4 8.2 2.4) (vector 0.7 2.3 4.6)) (vector (vector 6.4 5.6 0.3) (vector 9.4 8.8 2.5)) (vector (vector 1.4 3.7 5.3) (vector 1.8 3.4 5.6))))) (vector (vector (vector (vector (vector 7.0 0.5 3.8) (vector 4.7 0.6 8.1)) (vector (vector 9.0 2.9 3.6) (vector 7.8 9.3 9.0)) (vector (vector 0.2 5.5 3.0) (vector 7.7 0.9 7.7)))) (vector (vector (vector (vector 3.5 9.6 6.1) (vector 6.5 6.2 3.1)) (vector (vector 0.1 4.8 0.6) (vector 2.4 1.1 7.1)) (vector (vector 5.8 2.8 9.8) (vector 0.6 6.5 5.0)))) (vector (vector (vector (vector 3.6 3.4 6.8) (vector 0.8 0.4 6.5)) (vector (vector 9.6 4.0 2.1) (vector 3.3 3.8 0.8)) (vector (vector 5.1 5.7 3.8) (vector 1.5 0.2 1.3)))))))
; )
