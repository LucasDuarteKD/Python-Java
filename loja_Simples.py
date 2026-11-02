Somadacompra = 0

print("LOJINHA DO SEBASTIÂO\n\n")
print("itens\n")
print("A loja possui:\n\n1 - Maçã(R$2.99).\n2 - Banana(R$3.99).\n3 - Melância(R$5.99).\n4 - Mamão(R$8.99).\n5 - Abacate(R$7.99).\n")
print("Digite '0' para finalizar a compra\n")
diferentes = int(input("Digite quantas frutas diferentes você deseja: "))
for i in range(1,diferentes+1):
    frutas = int(input("Digite o código da fruta: "))
    if diferentes <= 5 and diferentes > 0:
        if frutas == 2:
            banana = int(input("Digite quantos cachos de banana: "))
            Somadacompra = (banana*3.99)+Somadacompra
        elif frutas == 1:
            maçã = int(input("Digite quantas maçãs: "))
            Somadacompra = (maçã*2.99)+Somadacompra
        elif frutas == 3:
            melancia = int(input("Digite quantas melâncias: "))
            Somadacompra = (melancia*5.99)+Somadacompra
        elif frutas == 4:
            mamão = int(input("Digite quantos mamões: "))
            Somadacompra = (mamão*8.99)+Somadacompra
        elif frutas == 5:
            abacate = int(input("Digite quantos abacates: "))
            Somadacompra = (abacate*7.99)+Somadacompra
        elif frutas == 0:
            print("Compra finalizada")
            break
        else:
            print("Opção Invalida")
    else:
        print("Compra finalizada")
print(f"O preço total da compra foi R${Somadacompra}")
