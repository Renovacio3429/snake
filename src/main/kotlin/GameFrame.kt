import javax.swing.JFrame

class GameFrame: JFrame() {

    init {
        this.add(GamePanel())
        this.title = "Snake"
        this.defaultCloseOperation = EXIT_ON_CLOSE
        this.isResizable = false
        this.pack()
        this.isVisible = true
        this.setLocationRelativeTo(null)
    }
}