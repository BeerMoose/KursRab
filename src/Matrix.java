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
    //��������� ������
    Matrix mult(Matrix a){
        Matrix result = new Matrix(this.n, 1);
        for (int i = 0; i < this.n ; i++)
            for (int j = 0; j < this.n ; j++)
                result.value[i][0] += this.value[i][j] * a.value[j][0];
        return result;
    }
    //��������� ������� �� ������
    Matrix mult(double k){
        Matrix result = new Matrix(this.n, this.m);
        for (int i = 0; i < this.n ; i++)
            for (int j = 0; j < this.m ; j++)
                result.value[i][0] = this.value[i][j] * k;
        return result;
    }
    //�������� ������
    Matrix minus(Matrix a){
        Matrix result = new Matrix(this.n, this.m);
        for (int i = 0; i < n ; i++)
            for (int j = 0; j < m ; j++)
                result.value[i][j] = this.value[i][j] - a.value[i][j];
        return result;
    }
    //����� ������
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
    //��� �������� f(xn)
    Matrix fXn(Matrix xn){
        Matrix result = new Matrix(this.n, 1);
        for (int i = 0; i < this.n - 1; i++) {
            for (int j = 0; j < this.n ; j++) {
                result.value[i][0] += this.value[i][j] * xn.value[j][0];
            }
            result.value[i][0] -= this.value[i][n];
        }

        result.value[n - 1][0] = 1; //����� ��� ��������� �� ���� 0

        for (int j = 0; j < n ; j++)
            result.value[n - 1][0] *= xn.value[j][0];

        result.value[n - 1][0] -= 1;
        return result;
    }
    //����� �������
    void printMatrix(){
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.m; j++)
                System.out.print(this.value[i][j] + " ");
            System.out.println();
        }
    }
    //������� �����������
    Matrix proizvod(Matrix xn){
        Matrix result = new Matrix(this.n, this.n);

        for (int i = 0; i < n - 1 ; i++)
            for (int j = 0; j < n; j++)
                result.value[i][j] = this.value[i][j];

        double pr = 1; //������������ ������ ���������
        for (int k = 0; k < n; k++)
            pr *= xn.value[k][0];

        for (int j = 0; j < n ; j++)
            result.value[n-1][j] = pr / xn.value[j][0];

        return result;

    }
    //���������� �������� �������. ����� ������� - ������
    Matrix inverse(){
        int i, j, k;
        int size = this.n;
        Matrix E = new Matrix(size, size);//��������� �������
        //���������� ��������� �������
        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                if (i == j)
                    E.value[i][j] = 1;
                else
                    E.value[i][j] = 0;
            }
        }
        //����� ����� ������� ������ (������� 0,1...size)
        for (k = 0; k < size; k++) {
            for (j = k + 1; j < size; j++) {
                this.value[k][j] = this.value[k][j] / this.value[k][k];//��� �������� k-�� ������ ������� A, ����� k-��� � �� ����, ����� �� ����������� ������� - a[k][k]
            }
            for (j = 0; j < size; j++) {
                E.value[k][j] = E.value[k][j] / this.value[k][k];//��� �������� k-�� ������ ������� e, ����� �� ����������� ������� - a[k][k]
            }
            this.value[k][k] = this.value[k][k] / this.value[k][k];//������� ���������������  ������������ - ����� �� ������ ����(�.� �������. 1 )
            //��� ������ ����, ������ k-�� ������
            if (k > 0) {//���� ����� ������� ������ �� ������
                for (i = 0; i < k; i++) {   //������, ����������� ���� k-��
                    for (j = 0; j < size; j++) {
                        E.value[i][j] = E.value[i][j] - E.value[k][j] * this.value[i][k];//��������� �������� ������� e,��� �� �������� � 0 -���  � ����������
                    }
                    for (j = size - 1; j >= k; j--) {
                        this.value[i][j] = this.value[i][j] - this.value[k][j] * this.value[i][k];//��������� �������� ������� a,��� �� �������� � ���������� � k-���
                    }
                }
            }
            for (i = k + 1; i < size; i++) {   //������, ����������� ���� k-��
                for (j = 0; j < size; j++) {
                    E.value[i][j] = E.value[i][j] - E.value[k][j] * this.value[i][k];
                }
                for (j = size - 1; j >= k; j--) {
                    this.value[i][j] = this.value[i][j] - this.value[k][j] * this.value[i][k];
                }
            }
                       }
        return E;//�� ����� �������� ������� ������ ���������� ��������� � �� ����� ��������� - ��������.
    }
}
