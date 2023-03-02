Tema 1 POO

    Pentru inceput, am folosit pentru reprezentarea cartilor o clasa abstracta Card, cu atributelele comune tuturor
cartilor.

    Apoi,pentru cele trei tipuri de carti,am creat cate o clasa care mosteneste clasa abstracta Card, la care am adaugat
atribute specifice fiecarui tip de carte reprezentat.Astfel,am creat clasele EnvironmentCard, MinionCard si HeroCard,
utilizand conceptele de mostenire si abstractizare.

    Dupa aceea am creat obiecte Card, folosind design pattern-ul Factory, astfel ca am implementat clasa CardFactory,
in care metoda createCardFromInput returneaza un obiect Card, dar care este instantiat cu clasa reprezentativa
EnvironmentCard, MinionCard sau HeroCard),in functie de input-ul venit (CardInput).

    Dupa,am reprezentat un pachet de carti cu ajutorul clasei Deck, ale carui atribute sunt numarul de carti din pachet
si o lista de obiecte de tip Card.

    Apoi, am reprezentat masa de joc cu ajutorul clasei GameTable, care are ca atribut o matrice de dimensiunea 4x5 care
contine elemente de tipul Card.In aceasta clasa,am implementat si metodele:
    -isRowFull, care verifica daca un rand este plin;
    -fullRow, care verifica in functie de numele cartii daca randul este plin in functie de fiecare jucator;
    -addCardOnRow, care adauga o carte pe un rand;
    -removeCardFromRow, care sterge o carte de pe un rand;
    -putOnTable, care pune cartea pe masa de joc in functie de numele acesteia.

  Dupa aceea, pentru a reprezenta un jucator, am folosit clasa Player, care are ca atribute urmatoarele:
	- numarul de pachete de carti
	- o lista cu pachetele jucatorului
	- un intreg care reprezinta numarul pachetului folosit in joc
	- un obiect de tipul HeroCard, care ilustreaza eroul jucatorului
	- o lista de carti care reprezinta cartile pe care jucatorul le are in mana in momentul de fata
	- un intreg ce reprezinta mana jucatorului
	si mai contine pe langa getteri si setteri o metoda pickFirstCard, pentru ca jucatorul sa poata avea in mana prima
carte din pachet.

    Apoi,in clasa Command am reprezentat toate comenzile,codurile de eroare si mesajele de eroare.

    Dupa,am reprezentat un joc prin clasa Game, care are ca atribute:
	- un array cu 2 Playeri
	- masa de joc
	- un intreg care reprezinta jucatorul curent (whoseTurn = 0 => primul jucator; whoseTurn = 1 => al doilea jucator)
	- un intreg ce reprezinta shuffleSeed
	si ca metode:
	- shufflePlayersPickedDeck, care amesteca pachetele alese de cei 2 jucatori;
	- endPlayerTurn, care ajuta jucatorii sa schimbe turele;
	- incrementMana
	- placeCard, care trateaza posibilitatile si erorile pentru a pune cartea pe masa de joc;
	- useEnvironmentCard, care trateaza posibilitatile si erorile pentru a folosi o carte de tip Environment;
	- cardUsesAttack, care implementeaza atacul dintre 2 carti;
	- setCurrentPlayerAlreadyAttackedCardsToFalse, care interzice celorlaltor carti sa mai atace,daca jucatorul deja a
	efectuat un atac;
	- cardUsesAbility, care implementeaza folosirea unei abilitati a unei carti de catre un jucator;
	- cardAttackHero, care implementeaza atacul asupra unui erou;
	- useHeroAbility, care implementeaza utilizarea abilitatii unui erou.

    Dupa aceea, pentru a incepe un joc conform regulilor, am creat clasa GameManager, care primeste input-ul pentru un
joc si creeaza un obiect de tip Game asociat,care va seta datele initiale ale jocului si apoi, la fiecare actiune, va
delega claselor anterioare responsabilitatile pentru a actualiza starea jocului in GameManager, pentru fiecare actiune,
se verifica ce tip de actiune este si se apeleaza metode ajutatoare pentru indeplinirea functionalitatilor asteptate.

   In final, pentru a avea suport pentru mai multe jocuri, am creat clasa MultipleGamesManager, care are rolul de a crea
un GameManager pentru fiecare joc in parte si de a incepe executia actiunilor acestuia,iar entry-point-ul folosit in
clasa Main intrebuinteaza aceasta clasa.