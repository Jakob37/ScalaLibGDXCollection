package gdx.scala.demo

import com.badlogic.gdx.{ApplicationAdapter, Gdx, Input}
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{BitmapFont, SpriteBatch}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.bullet.Bullet
import com.badlogic.gdx.utils.TimeUtils

class GdxScalaDemoGame extends ApplicationAdapter {

  private[demo] var batch: SpriteBatch = null
  private[demo] var img: Texture = null

  private var font:BitmapFont = _

  private val level_width:Int = 800
  private val level_height:Int = 400

  private var pos:Vector2 = _

  private var hunger:Float = 0

  private var current_time:Long = _

  override def create() {
    Bullet.init()
    batch = new SpriteBatch
    font = new BitmapFont()
    img = new Texture("assets\\tamagotchi_spritesheet.png")
    current_time = TimeUtils.millis()

    val xpos = level_width / 2 - 400 / 2
    val ypos = level_height / 2 - 100 / 2
    pos = new Vector2(xpos, ypos)
  }

  override def render() {

    val elapsed_time = TimeUtils.timeSinceMillis(current_time)
    current_time = TimeUtils.millis()

    hunger += elapsed_time.toFloat / 1000.0f

    Gdx.gl.glClearColor(0.3f, 0.3f, 0.5f, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    batch.begin()

    val stretchWidth = 200
    val stretchHeight = 200
    val srcX = getSpriteX(hunger)
    val srcY = 0

    if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && hunger < 12) {
      feed()
    }

    updatePosition()

    batch.draw(img, pos.x, pos.y, stretchWidth, stretchHeight, srcX, srcY, 32, 32, false, false)

    if (hunger < 12) {
      font.draw(batch, "Hunger: %.1f".format(hunger), level_height - 100, 100)
      font.draw(batch, "Feed with space. Move with arrows.".format(hunger), level_height - 100, 60)
    }
    else {
      font.draw(batch, "You died :(".format(hunger), level_height - 100, 100)
    }

    batch.end()
  }

  private def feed():Unit = {
    hunger -= 0.5f
    if (hunger < 0) {
      hunger = 0
    }
  }

  private def updatePosition():Unit = {

    val speed = 2

    if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      pos.x -= speed
    }
    else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      pos.x += speed
    }
    else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
      pos.y += speed
    }
    else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      pos.y -= speed
    }
  }

  private def getSpriteX(hunger:Float):Int = {

    val character_width = 32

    var mult_factor = 0

    if (hunger < 3) {
      mult_factor = 0
    }
    else if (hunger < 6) {
      mult_factor = 1
    }
    else if (hunger < 9) {
      mult_factor = 2
    }
    else if (hunger < 12) {
      mult_factor = 3
    }
    else {
      mult_factor = 4
    }

    character_width * mult_factor
  }
}
