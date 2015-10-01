import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Solution {
    FileWriter fw = null;
    int n;
    double eps, beta, gamma;
    double a, b;
    Matrix f;
    Matrix x0;
    Matrix xn , newXn;
    int count;
    int flagMetod;
    Solution(int n, double eps, double beta, double a, double b, Matrix xn, int flagMetod){
        this.n = n;this.eps = eps;this.beta = beta;this.a = a;this.b = b;
        this.xn = xn; this.x0 = xn; this.gamma = beta * beta; this.flagMetod = flagMetod;
        f = generateF();//Генерируем матрицу с двойками по диагонали
        switch (flagMetod){//Выбираем как будем пересчитывать бету
            case 1:metod1Mod();break;
            case 4:metod3Mod();break;
            case 5:metod4();break;
            case 6:metod5();break;
            case 7:metod6();break;
            default:calculateForMe();
        }
      }
    //Генерируем матрицу с двойками по диагонали
    Matrix generateF(){
        Matrix result = new Matrix(n,n+1);
        for(int i = 0; i < n - 1; i++ ) {
            for (int j = 0; j < n; j++)
                result.value[i][j] = (i == j) ? 2 : 1;
            result.value[i][n] = n + 1;
            result.value[n-1][i] = 1;
        }
        result.value[n-1][n-1] = 1;
        result.value[n-1][n] = 1;
        return result;
    }
    //Считаем xn
    void calculateForMe(){
        double tempSkalyar;//Промежуточная переменная
        Matrix tempMatrix;// f(xn) * f'(xn)
        count = 0;
        do{
            if(count > 500000){     //Если итераций больше чем 500.000
                System.out.println(beta);//то это бред
                return;//выходим
            }
            double norm = f.fXn(xn).norma();
            tempMatrix = (f.proizvod(xn)).mult(f.fXn(xn));
            tempSkalyar = beta * (norm * norm) / (tempMatrix.norma() * tempMatrix.norma());
            tempMatrix = tempMatrix.mult(tempSkalyar);
            newXn = xn.minus(tempMatrix);
            switch (flagMetod){//Выбираем как будем пересчитывать бету
                case 0:metod1();break;
                case 2:metod2();break;
                case 3:metod3();break;
            }
            count++;
        }while (f.fXn(xn).norma() > eps);
        //printNorm(annNorm);
    }
    void metod1(){
        beta = Math.min(1, beta * f.fXn(xn).norma() / f.fXn(newXn).norma());
        xn = newXn;
    }
    void metod2(){
        beta = Math.min(1, gamma * f.fXn(xn).norma() / ( f.fXn(newXn).norma() * beta));
        gamma = gamma * f.fXn(xn).norma() / f.fXn(newXn).norma();
        xn = newXn;
    }
    void metod3(){
        double newBeta;
        double skalyar;
        skalyar = gamma * f.fXn(x0).norma() / beta;
        newBeta = Math.min(1, skalyar / f.fXn(newXn).norma());
        gamma = newBeta * gamma / beta;
        xn = newXn;
        beta = newBeta;
    }
    void metod4(){
        //Тут всё строго по алгоритму
        count = 0;
        Matrix deltaX0 = f.proizvod(x0).inverse().mult(f.fXn(x0).mult(-1));
        Matrix x1 = x0.plus(deltaX0.mult(beta));
        while (f.fXn(x1).norma() > eps && count < 500000) {
            Matrix deltaX1 = f.proizvod(x1).inverse().mult(f.fXn(x1).mult(-1));
            beta = Math.min(1, beta * deltaX0.norma() * f.fXn(x0).norma() / (deltaX1.norma() * f.fXn(x1).norma()));
            Matrix x2 = x1.plus(deltaX1.mult(beta));
            x0 = x1; x1 = x2; deltaX0 = deltaX1;
            count++;
        }
        xn = x1;
    }
    void metod3Mod(){
        count = 0;
        double gamma0 = beta * beta;
        double gamma = gamma0;
        double newBeta;

        Matrix deltaX0 = f.proizvod(x0).inverse().mult(f.fXn(x0).mult(-1));
        Matrix x1 = x0.plus(deltaX0.mult(Math.pow(beta, 1.0/2)));

        while (f.fXn(x1).norma() > eps && count < 500000) {
            newBeta = Math.min(1, gamma  * Math.pow(f.fXn(x0).norma(),2) / (Math.pow(f.fXn(x1).norma(),2) * beta));
            gamma = newBeta / beta * gamma0;
            Matrix deltaX1 = f.proizvod(x1).inverse().mult(f.fXn(x1).mult(-1));
            x1 = x1.plus(deltaX1.mult(Math.pow(newBeta, 1.0/2)));
            beta = newBeta;
            count++;
        }
        xn = x1;
    }
    void metod1Mod(){
        count = 0;
        beta = Math.pow(beta, 1.0/2);
        Matrix deltaX0 = f.proizvod(x0).inverse().mult(f.fXn(x0).mult(-1));
        Matrix x1 = x0.plus(deltaX0.mult(Math.pow(beta, 1.0/2)));

        while (f.fXn(x1).norma() > eps && count < 500000) {
            beta = Math.min(1, beta  * Math.pow(f.fXn(x0).norma(),2) / (Math.pow(f.fXn(x1).norma(),2)));
            x0 = x1;
            deltaX0 = f.proizvod(x0).inverse().mult(f.fXn(x0).mult(-1));
            x1 = x0.plus(deltaX0.mult(Math.pow(beta, 1.0/2)));
            count++;
        }
        xn = x1;
    }
    void metod5(){
        count = 0;
        Matrix x1, x2, deltaX1;
        double gamma =  beta * beta;
        double  newBeta;

        Matrix deltaX0 = f.proizvod(x0).inverse().mult(f.fXn(x0).mult(-1));
        x1 = x0.plus(deltaX0.mult(Math.pow(beta, 1.0/2)));

        while (f.fXn(x1).norma() > eps && count < 500000) {
            newBeta = Math.min(1, (f.fXn(x0).norma() * gamma) / (f.fXn(x1).norma() * beta));
            deltaX1 = f.proizvod(x1).inverse().mult(f.fXn(x1).mult(-1));
            x2 = x1.plus(deltaX1.mult(Math.pow(newBeta, 1.0/2)));
            gamma = newBeta / beta * gamma * f.fXn(x1).norma()/f.fXn(x2).norma();
            beta = newBeta; x1 = x2;
            count++;
        }
        xn = x1;
    }
    void metod6(){
        count = 0;
        Matrix x1, x2, deltaX1;
        double gamma =  beta * beta * beta * beta;
        double  newBeta;

        Matrix deltaX0 = f.proizvod(x0).inverse().mult(f.fXn(x0).mult(-1));
        x1 = x0.plus(deltaX0.mult(Math.pow(beta, 1.0/2)));

        while (f.fXn(x1).norma() > eps && count < 500000) {
            newBeta = Math.min(1,Math.pow( (f.fXn(x0).norma() * gamma) / (f.fXn(x1).norma() * beta * beta), 1.0/2));
            deltaX1 = f.proizvod(x1).inverse().mult(f.fXn(x1).mult(-1));
            x2 = x1.plus(deltaX1.mult(Math.pow(newBeta, 1.0/2)));
            gamma = newBeta * newBeta / (beta * beta) * gamma * f.fXn(x0).norma()/f.fXn(x2).norma();
            beta = newBeta;
            x0 = x1;x1 = x2;
            count++;
        }
        xn = x1;
    }
    //Генерация html кода для читаемого вывода
    String[] printAll(){
        String[] result = new String[3];    //Эта хня - массив строк, хранит 3 значения
        result[0] = count + ""; //Кол-во итераций
        result[1] =  f.fXn(xn).norma() + ""; //Невязка
        StringBuilder allX = new StringBuilder("<html>");
        for (int i = 0; i < n; i++) {
            allX.append("X<sub>" + (i+1) + "</sub>=" + xn.value[i][0] + "<br>");
        }
        result[2] = allX.toString();//Сами иксы
        return result;
    }
}
