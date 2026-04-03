#include <iostream>

using namespace std;

int main() {
    char op;
    double num1, num2;

    cout << "========= CALCULADORA C++ =========" << endl;
    
    // Solicita o primeiro número
    cout << "Digite o primeiro numero: ";
    cin >> num1;

    // Solicita o operador
    cout << "Digite o operador (+, -, *, /): ";
    cin >> op;

    // Solicita o segundo número
    cout << "Digite o segundo numero: ";
    cin >> num2;

    cout << "-----------------------------------" << endl;

    // Estrutura de decisão para a operação
    switch(op) {
        case '+':
            cout << "Resultado: " << num1 << " + " << num2 << " = " << num1 + num2 << endl;
            break;

        case '-':
            cout << "Resultado: " << num1 << " - " << num2 << " = " << num1 - num2 << endl;
            break;

        case '*':
            cout << "Resultado: " << num1 << " * " << num2 << " = " << num1 * num2 << endl;
            break;

        case '/':
            // Verificação de divisão por zero
            if (num2 != 0) {
                cout << "Resultado: " << num1 << " / " << num2 << " = " << num1 / num2 << endl;
            } else {
                cout << "Erro: Divisao por zero nao e permitida!" << endl;
            }
            break;

        default:
            // Caso o usuário digite um operador inválido
            cout << "Erro: Operador invalido!" << endl;
            break;
    }

    cout << "===================================" << endl;

    return 0;
}