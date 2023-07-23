import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Graphics
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.util.Random
import javax.swing.JPanel
import javax.swing.Timer
import kotlin.properties.Delegates

var SCREEN_WIDTH: Int = 600
var SCREEN_HEIGHT: Int = 600
var UNIT_SIZE: Int = 25
var GAME_UNITS: Int = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE
var DELAY: Int = 75

class GamePanel : JPanel(), ActionListener {

    val x: IntArray = IntArray(GAME_UNITS)
    val y: IntArray = IntArray(GAME_UNITS)
    private var bodyParts: Int = 6
    private var appleEaten: Int = 0
    private var appleX: Int = 0
    private var appleY: Int = 0
    var direction: Char = 'R'
    private var running: Boolean = false
    private var timer: Timer by Delegates.notNull()
    private var random: Random = Random()

    init {
        this.preferredSize = Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)
        this.background = Color.BLACK
        this.isFocusable = true
        this.addKeyListener(KeyApapterExt())
        startGame()
    }

    private fun startGame() {
        newApple()
        running = true
        timer = Timer(DELAY, this)
        timer.start()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        draw(g)
    }

    private fun draw(g: Graphics) {
        if (running) {
            g.color = Color.RED
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE)

            for (i in 0 until bodyParts) {
                if (i == 0) {
                    g.color = Color.GREEN
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE)
                } else {
                    g.color = Color(random.nextInt(255), random.nextInt(255), random.nextInt(255))
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE)
                }
            }

            g.color = Color.RED
            g.font = Font("Ink Free", Font.BOLD, 40)
            val metrics: FontMetrics = getFontMetrics(g.font)
            g.drawString("Score: $appleEaten", (SCREEN_WIDTH - metrics.stringWidth("Apple eaten: $appleEaten")) / 2, g.font.size)
        } else {
            gameOver(g)
        }
    }

    private fun move() {
        for (i in bodyParts downTo 1) {
            x[i] = x[i - 1]
            y[i] = y[i - 1]
        }

        when (direction) {
            'U' -> y[0] = y[0] - UNIT_SIZE
            'D' -> y[0] = y[0] + UNIT_SIZE
            'L' -> x[0] = x[0] - UNIT_SIZE
            'R' -> x[0] = x[0] + UNIT_SIZE
        }
    }

    private fun newApple() {
        appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE
        appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE
    }

    private fun checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++
            appleEaten++
            newApple()
        }
    }

    private fun checkCollisions() {
        for (i in bodyParts downTo 1) {
            if ((x[0] == x[i]) && (y[0] == y[i]))
                running = false
        }

        if (x[0] < 0) running = false
        if (x[0] > SCREEN_WIDTH) running = false
        if (y[0] < 0) running = false
        if (y[0] > SCREEN_HEIGHT) running = false
        if (!running) timer.stop()
    }

    private fun gameOver(g: Graphics) {
        g.color = Color.RED
        g.font = Font("Ink Free", Font.BOLD, 40)
        val metrics1: FontMetrics = getFontMetrics(g.font)
        g.drawString("Score: $appleEaten", (SCREEN_WIDTH - metrics1.stringWidth("Apple eaten: $appleEaten")) / 2, g.font.size)


        g.color = Color.RED
        g.font = Font("Ink Free", Font.BOLD, 75)
        val metrics2: FontMetrics = getFontMetrics(g.font)
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Gave Over")) / 2, SCREEN_HEIGHT / 2)
    }

    override fun actionPerformed(e: ActionEvent?) {
        if (running) {
            move()
            checkApple()
            checkCollisions()
        }
        repaint()
    }

    inner class KeyApapterExt : KeyAdapter() {

        override fun keyPressed(e: KeyEvent) {
            when (e.keyCode) {
                KeyEvent.VK_LEFT -> if (direction != 'R') direction = 'L'
                KeyEvent.VK_RIGHT -> if (direction != 'L') direction = 'R'
                KeyEvent.VK_UP -> if (direction != 'D') direction = 'U'
                KeyEvent.VK_DOWN -> if (direction != 'U') direction = 'D'
            }
        }
    }
}