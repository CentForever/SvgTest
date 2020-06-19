package jp.shts.android.trianglelabelview

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setTitle(R.string.app_name)
        stateMachine.initialState(State.None)

        findViewById<TextView>(R.id.mTextView).postDelayed({
            findViewById<TextView>(R.id.mTextView).isEnabled = true
            stateMachine.executeTransition(Transition.Canceled)
        },500)
    }

    val measuredWidth: Int
        get() = 20 and View.MEASURED_SIZE_MASK

    enum class State { None,Select }

    enum class Transition { Selected, Canceled }

    private val stateMachine: StateMachine<State, Transition> by lazy {
        StateMachine<State, Transition>().apply {

            state(State.None) {
                lastState?.apply {
                    println("I have been ${State.None} from $this by $lastTransition")
                } ?: with(State.None) {
                    println("I have been initialState with $this")
                }
            }

            state(State.Select) {
                lastState?.apply {
                    println("I have been ${State.Select} from $this by $lastTransition")
                } ?: with(State.Select) {
                    println("I have been initialState with $this")
                }
            }
            addTransition(State.None, Transition.Selected, State.Select)
            addTransition(State.Select, Transition.Canceled, State.None)
        }
    }

}

