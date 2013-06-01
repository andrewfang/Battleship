package battleship;

class Test {
    public static void main(String[] ignored) {
	SelfBoard s = new SelfBoard();
	Coordinate c = new Coordinate("a1-a5");
	Ship ship = new Ship(Shipname.CARRIER);
	s.place(ship, c);
	System.out.println(s);
	EnemyBoard e = new EnemyBoard(s);
	System.out.println(e);
	Move m = new Move(3, 3);
	e.makeMove(m);
	e.makeMove(new Move(1, 1));
	System.out.println(e);
    }
}