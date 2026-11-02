    except ValueError:
        print("Preço e estoque devem ser números válidos e não negativos.")
        return

    ws.append([codigo, nome, categoria, preco, estoque])
    wb.save(ARQUIVO)
    print("Produto cadastrado com sucesso!")


def listar_produtos():
    _, ws = carregar_planilha()
    print("\n--- Produtos cadastrados ---")
    if ws.max_row == 1:
        print("Nenhum produto cadastrado.")
        return

    print(f"{'Código':<12} {'Nome':<25} {'Categoria':<18} {'Preço':>10} {'Estoque':>10}")
    print("-" * 80)
    for codigo, nome, categoria, preco, estoque in ws.iter_rows(min_row=2, values_only=True):
        print(f"{str(codigo):<12} {str(nome)[:24]:<25} {str(categoria or '')[:17]:<18} "
              f"R$ {float(preco):>7.2f} {int(estoque):>10}")


def buscar_produto():
    _, ws = carregar_planilha()
    termo = input("\nDigite o código ou parte do nome: ").strip().lower()
    encontrados = []

    for linha in ws.iter_rows(min_row=2, values_only=True):
        codigo, nome, categoria, preco, estoque = linha
        if termo == str(codigo).lower() or termo in str(nome).lower():
            encontrados.append(linha)

    if not encontrados:
        print("Nenhum produto encontrado.")
        return

    for p in encontrados:
        print(f"Código: {p[0]} | Nome: {p[1]} | Categoria: {p[2]} | "
              f"Preço: R$ {float(p[3]):.2f} | Estoque: {p[4]}")


def atualizar_produto():
    wb, ws = carregar_planilha()
    codigo = input("\nCódigo do produto que deseja atualizar: ").strip()

    for linha in ws.iter_rows(min_row=2):
        if str(linha[0].value) == codigo:
            print("Deixe um campo vazio para manter o valor atual.")
            nome = input(f"Nome [{linha[1].value}]: ").strip()
            categoria = input(f"Categoria [{linha[2].value or ''}]: ").strip()
            preco_txt = input(f"Preço [{linha[3].value}]: ").strip()
            estoque_txt = input(f"Estoque [{linha[4].value}]: ").strip()

            try:
                if nome:
                    linha[1] = nome
                if categoria:
                    linha[2] = categoria
                if preco_txt:
                    preco = float(preco_txt.replace(",", "."))
                    if preco < 0:
                        raise ValueError
                    linha[3] = preco
                if estoque_txt:
                    estoque = int(estoque_txt)
                    if estoque < 0:
                        raise ValueError
                    linha[4] = estoque
            except ValueError:
                print("Valor inválido. Nenhuma alteração foi salva.")
                return

            wb.save(ARQUIVO)
            print("Produto atualizado com sucesso!")
            return

    print("Código não encontrado.")


def remover_produto():
    wb, ws = carregar_planilha()
    codigo = input("\nCódigo do produto que deseja remover: ").strip()

    for numero_linha in range(2, ws.max_row + 1):
        if str(ws.cell(numero_linha, 1).value) == codigo:
            confirmacao = input("Tem certeza que deseja remover? (s/n): ").strip().lower()
            if confirmacao == "s":
                ws.delete_rows(numero_linha)
                wb.save(ARQUIVO)
                print("Produto removido.")
            else:
                print("Operação cancelada.")
            return

    print("Código não encontrado.")


def menu():
    preparar_planilha()
    while True:
        print("\n========== SISTEMA DA LOJA ==========")
        print("1 - Cadastrar produto")
        print("2 - Listar produtos")
        print("3 - Buscar produto")
        print("4 - Atualizar produto")
        print("5 - Remover produto")
        print("0 - Sair")
        opcao = input("Escolha uma opção: ").strip()

        if opcao == "1":
            adicionar_produto()
        elif opcao == "2":
            listar_produtos()
        elif opcao == "3":
            buscar_produto()
        elif opcao == "4":
            atualizar_produto()
        elif opcao == "5":
            remover_produto()
        elif opcao == "0":
            print(f"Dados salvos em: {ARQUIVO.resolve()}")
            print("Até logo!")
            break
        else:
            print("Opção inválida. Tente novamente.")

