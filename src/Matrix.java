public class Matrix {
    int n, m;
    double[][] value;
    double q = 1;
    Matrix(Matrix a){
        this.n = a.n;
        this.m = a.m;
        this.value = a.value;
    }
    Matrix(double[][]a){
        this.n = a.length;
        this.m = a[0].length;
        this.value = a;
    }
    Matrix(int n, int m){
        this.n = n;
        this.m = m;
        value = new double[n][m];
    }
    //Умножение матриц
    Matrix mult(Matrix a){
        Matrix result = new Matrix(this.n, 1);
        for (int i = 0; i < this.n ; i++)
            for (int j = 0; j < this.n ; j++)
                result.value[i][0] += this.value[i][j] * a.value[j][0];
        return result;
    }
    //Умножение матрицы на скаляр
    Matrix mult(double k){
        Matrix result = new Matrix(this.n, this.m);
        for (int i = 0; i < this.n ; i++)
            for (int j = 0; j < this.m ; j++)
                result.value[i][0] = this.value[i][j] * k;
        return result;
    }
    //Разность матриц
    Matrix minus(Matrix a){
        Matrix result = new Matrix(this.n, this.m);
        for (int i = 0; i < n ; i++)
            for (int j = 0; j < m ; j++)
                result.value[i][j] = this.value[i][j] - a.value[i][j];
        return result;
    }
    //Сумма матриц
    Matrix plus(Matrix a){
        Matrix result = new Matrix(this.n, this.m);
        for (int i = 0; i < n ; i++)
            for (int j = 0; j < m ; j++)
                result.value[i][j] = this.value[i][j] + a.value[i][j];
        return result;
    }
    double norma(){
        double result = 0;
        for (int i = 0; i < this.n; i++)
            for (int j = 0; j < this.m; j++)
                result += this.value[i][j] * this.value[i][j];
        return Math.pow(result, 1.0 / 2);
    }
    //Даёт значение f(xn)
    Matrix fXn(Matrix xn){
        Matrix result = new Matrix(this.n, 1);
        for (int i = 0; i < this.n - 1; i++) {
            for (int j = 0; j < this.n ; j++) {
                result.value[i][0] += this.value[i][j] * xn.value[j][0];
            }
            result.value[i][0] -= this.value[i][n];
        }

        result.value[n - 1][0] = 1; //Чтобы при умножении не дало 0

        for (int j = 0; j < n ; j++)
            result.value[n - 1][0] *= xn.value[j][0];

        result.value[n - 1][0] -= 1;
        return result;
    }
    //Вывод матрицы
    void printMatrix(){
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.m; j++)
                System.out.print(this.value[i][j] + " ");
            System.out.println();
        }
    }
    //Считает производную
    Matrix proizvod(Matrix xn){
        Matrix result = new Matrix(this.n, this.n);

        for (int i = 0; i < n - 1 ; i++)
            for (int j = 0; j < n; j++)
                result.value[i][j] = this.value[i][j];

        double pr = 1; //Произведение нижних элементов
        for (int k = 0; k < n; k++)
            pr *= xn.value[k][0];

        for (int j = 0; j < n ; j++)
            result.value[n-1][j] = pr / xn.value[j][0];

        return result;

    }
    //Возвращает обратную матрицу. Метод Жордана - Гаусса
    Matrix inverse(){
        int i, j, k;
        int size = this.n;
        Matrix E = new Matrix(size, size);//единичная матрица
        //заполнение единичной матрицы
        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                if (i == j)
                    E.value[i][j] = 1;
                else
                    E.value[i][j] = 0;
            }
        }
        //Задаём номер ведущей строки (сначала 0,1...size)
        for (k = 0; k < size; k++) {
            for (j = k + 1; j < size; j++) {
                this.value[k][j] = this.value[k][j] / this.value[k][k];//все элементы k-ой строки матрицы A, кроме k-ого и до него, делим на разрешающий элемент - a[k][k]
            }
            for (j = 0; j < size; j++) {
                E.value[k][j] = E.value[k][j] / this.value[k][k];//все элементы k-ой строки матрицы e, делим на разрешающий элемент - a[k][k]
            }
            this.value[k][k] = this.value[k][k] / this.value[k][k];//элемент соответствующий  разрещающему - делим на самого себя(т.е получит. 1 )
            //идём сверху вниз, обходя k-ую строку
            if (k > 0) {//если номер ведущей строки не первый
                for (i = 0; i < k; i++) {   //строки, находящиеся выше k-ой
                    for (j = 0; j < size; j++) {
                        E.value[i][j] = E.value[i][j] - E.value[k][j] * this.value[i][k];//Вычисляем элементы матрицы e,идя по столбцам с 0 -ого  к последнему
                    }
                    for (j = size - 1; j >= k; j--) {
                        this.value[i][j] = this.value[i][j] - this.value[k][j] * this.value[i][k];//Вычисляем элементы матрицы a,идя по столбцам с последнего к k-ому
                    }
                }
            }
            for (i = k + 1; i < size; i++) {   //строки, находящиеся ниже k-ой
                for (j = 0; j < size; j++) {
                    E.value[i][j] = E.value[i][j] - E.value[k][j] * this.value[i][k];
                }
                for (j = size - 1; j >= k; j--) {
                    this.value[i][j] = this.value[i][j] - this.value[k][j] * this.value[i][k];
                }
            }
                       }
        return E;//На месте исходной матрицы должна получиться единичная а на месте единичной - обратная.
    }
}
