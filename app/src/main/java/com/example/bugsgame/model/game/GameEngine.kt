package com.example.bugsgame.model.game

import com.example.bugsgame.model.GameSettings
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class GameEngine(
    private val random: Random = Random.Default
) {
    var state: GameEngineState = GameEngineState()              //Состояния
        private set

    private var nextBugId: Long = 1L                            //Уникальные id жуков
    private var elapsedSec: Float = 0f                          //Сколько секунд прошло с начала раунда
    private var spawnCooldownSec: Float = 0f                    //Таймер обычного спавна
    private var bonusCooldownSec: Float = 0f                    //Таймер появления редкого жука
    private var currentSettings: GameSettings = GameSettings()  //Настройки текущего раунда
    private var bugs: MutableList<Bug> = mutableListOf()        //Список жуков

    //Начало нового раунда
    fun startRound(settings: GameSettings) {
        currentSettings = settings.copy(
            gameSpeed = settings.gameSpeed.coerceAtLeast(1),
            maxBugs = settings.maxBugs.coerceAtLeast(1),
            bonusIntervalSec = settings.bonusIntervalSec.coerceAtLeast(1),
            roundDurationSec = settings.roundDurationSec.coerceAtLeast(1),
        )

        nextBugId = 1L
        elapsedSec = 0f
        spawnCooldownSec = GameConfig.SPAWN_INTERVAL_SEC
        bonusCooldownSec = currentSettings.bonusIntervalSec.toFloat()
        bugs = mutableListOf()

        val initialBugs = minOf(
            GameConfig.INITIAL_BUGS,
            currentSettings.maxBugs,
        )

        repeat(initialBugs) {
            bugs += createBug(initial = true)
        }

        publishState(
            phase = GamePhase.RUNNING,
            stats = RoundStats(),
        )
    }

    //Обновление кадров игры
    fun update(dtSec: Float) {
        if (state.phase != GamePhase.RUNNING) return
        if (dtSec <= 0f) return

        val safeDt = dtSec.coerceIn(0f, GameConfig.MAX_FRAME_DT_SEC)
        if (safeDt <= 0f) return

        elapsedSec += safeDt

        val durationSec = currentSettings.roundDurationSec.toFloat()

        if (elapsedSec >= durationSec) {
            elapsedSec = durationSec
            publishState(phase = GamePhase.FINISHED)
            return
        }

        bugs = bugs
            .map { updateBugMovement(it, safeDt) }
            .toMutableList()

        bugs = bugs
            .mapNotNull { updateBugLifetime(it, safeDt) }
            .toMutableList()

        updateSpawning(safeDt)

        publishState(phase = GamePhase.RUNNING)
    }

    //Обработка нажатия по игровому полю
    fun handleTap(x: Float, y: Float): TapResult {
        if (state.phase != GamePhase.RUNNING) return TapResult.Ignored
        if (!GameField.isInside(x, y)) return TapResult.Ignored

        val hitBug = findHitBug(x, y)

        if (hitBug != null) {
            bugs.remove(hitBug)

            val updatedStats = state.stats.copy(
                score = state.stats.score + hitBug.type.points,
                hits = state.stats.hits + 1,
            )

            publishState(
                phase = GamePhase.RUNNING,
                stats = updatedStats,
            )

            return TapResult.Hit(
                bug = hitBug,
                points = hitBug.type.points,
            )
        }

        val updatedStats = state.stats.copy(
            score = maxOf(
                GameConfig.MIN_SCORE,
                state.stats.score - GameConfig.MISS_PENALTY,
            ),
            misses = state.stats.misses + 1,
        )

        publishState(
            phase = GamePhase.RUNNING,
            stats = updatedStats,
        )

        return TapResult.Miss
    }

    fun pause() {
        if (state.phase == GamePhase.RUNNING) {
            publishState(phase = GamePhase.PAUSED)
        }
    }

    fun resume() {
        if (state.phase == GamePhase.PAUSED) {
            publishState(phase = GamePhase.RUNNING)
        }
    }

    //Движение жука
    private fun updateBugMovement(bug: Bug, dtSec: Float): Bug {
        val velocityX = cos(bug.directionRad) * bug.speed
        val velocityY = sin(bug.directionRad) * bug.speed

        var newX = bug.x + velocityX * dtSec
        var newY = bug.y + velocityY * dtSec

        var newVelocityX = velocityX
        var newVelocityY = velocityY
        var bounced = false

        val minX = bug.radius
        val maxX = GameField.WIDTH - bug.radius
         val minY = bug.radius
        val maxY = GameField.HEIGHT - bug.radius

        if (newX < minX && velocityX < 0f) {
            newX = minX
            newVelocityX = -velocityX
            bounced = true
        } else if (newX > maxX && velocityX > 0f) {
            newX = maxX
            newVelocityX = -velocityX
            bounced = true
        }

        if (newY < minY && velocityY < 0f) {
            newY = minY
            newVelocityY = -velocityY
            bounced = true
        } else if (newY > maxY && velocityY > 0f) {
            newY = maxY
            newVelocityY = -velocityY
            bounced = true
        }

        newX = GameField.clampX(newX, bug.radius)
        newY = GameField.clampY(newY, bug.radius)

        val newDirection = if (bounced) {
            atan2(newVelocityY, newVelocityX)
        } else {
            bug.directionRad
        }

        return Bug(
            id = bug.id,
            type = bug.type,
            x = newX,
            y = newY,
            directionRad = newDirection,
            speed = bug.speed,
            size = bug.size,
            remainingLifetimeSec = bug.remainingLifetimeSec,
        )
    }

    //Уменьшает время жизни жука
    private fun updateBugLifetime(bug: Bug, dtSec: Float): Bug? {
        val remaining = bug.remainingLifetimeSec ?: return bug
        val updatedRemaining = remaining - dtSec

        if (updatedRemaining <= 0f) {
            return null
        }

        return Bug(
            id = bug.id,
            type = bug.type,
            x = bug.x,
            y = bug.y,
            directionRad = bug.directionRad,
            speed = bug.speed,
            size = bug.size,
            remainingLifetimeSec = updatedRemaining,
        )
    }

    //Обновление таймера спавна новых жуков
    private fun updateSpawning(dtSec: Float) {
        spawnCooldownSec -= dtSec

        if (spawnCooldownSec <= 0f &&
            bugs.size < currentSettings.maxBugs
        ) {
            bugs += createBug(initial = false)
            spawnCooldownSec = GameConfig.SPAWN_INTERVAL_SEC
        }

        bonusCooldownSec -= dtSec

        if (bonusCooldownSec <= 0f) {
            bonusCooldownSec = currentSettings.bonusIntervalSec.toFloat()

            val rareCount = bugs.count { it.type == BugType.RARE }
            val hasFreeSlot = bugs.size < currentSettings.maxBugs

            if (rareCount < GameConfig.MAX_RARE_ON_FIELD && hasFreeSlot) {
                bugs += createBug(
                    initial = false,
                    forcedType = BugType.RARE,
                )
            }
        }
    }

    //Поиск ближайшего к точке тапа жука, который попадает в зону поражения.
    private fun findHitBug(x: Float, y: Float): Bug? {
        var bestBug: Bug? = null
        var bestDistanceSquared = Float.MAX_VALUE

        bugs.forEach { bug ->
            val dx = bug.x - x
            val dy = bug.y - y
            val distanceSquared = dx * dx + dy * dy

            val hitRadius = bug.radius + GameConfig.TAP_TOLERANCE_LOGICAL
            val hitRadiusSquared = hitRadius * hitRadius

            if (distanceSquared <= hitRadiusSquared &&
                distanceSquared < bestDistanceSquared
            ) {
                bestBug = bug
                bestDistanceSquared = distanceSquared
            }
        }

        return bestBug
    }

    //Создание нового жука
    private fun createBug(
        initial: Boolean,
        forcedType: BugType? = null,
    ): Bug {
        val type = forcedType ?: pickBugType()
        val radius = type.size / 2f

        val spawnPoint = if (initial) {
            createInitialSpawnPoint(radius)
        } else {
            createEdgeSpawnPoint(radius)
        }

        return Bug(
            id = nextBugId++,
            type = type,
            x = spawnPoint.x,
            y = spawnPoint.y,
            directionRad = spawnPoint.directionRad,
            speed = type.baseSpeed * GameConfig.speedMultiplier(
                currentSettings.gameSpeed,
            ),
            size = type.size,
            remainingLifetimeSec = type.lifetimeSec,
        )
    }

    //Выбор типа жука
    private fun pickBugType(): BugType {
        val rareCount = bugs.count { it.type == BugType.RARE }

        val allowedTypes = BugType.entries.filter { type ->
            type != BugType.RARE ||
                rareCount < GameConfig.MAX_RARE_ON_FIELD
        }

        val totalWeight = allowedTypes.sumOf { it.spawnWeight }
        if (totalWeight <= 0) return BugType.NORMAL

        var roll = random.nextInt(totalWeight)

        allowedTypes.forEach { type ->
            if (roll < type.spawnWeight) {
                return type
            }
            roll -= type.spawnWeight
        }

        return BugType.NORMAL
    }

    //Стартовая позиция внутри поля.
    private fun createInitialSpawnPoint(radius: Float): SpawnPoint {
        val margin = radius + GameConfig.SPAWN_MARGIN

        return SpawnPoint(
            x = randomBetween(
                from = margin,
                to = GameField.WIDTH - margin,
            ),
            y = randomBetween(
                from = margin,
                to = GameField.HEIGHT - margin,
            ),
            directionRad = random.nextFloat() * TWO_PI,
        )
    }

    //Стартовая позиция у края экрана
    private fun createEdgeSpawnPoint(radius: Float): SpawnPoint {
        val margin = radius + GameConfig.SPAWN_MARGIN
        val minX = margin
        val maxX = GameField.WIDTH - margin
        val minY = margin
        val maxY = GameField.HEIGHT - margin

        return when (random.nextInt(4)) {
            // Левая сторона, движение вправо.
            0 -> SpawnPoint(
                x = minX,
                y = randomBetween(minY, maxY),
                directionRad = 0f + randomSpread(),
            )

            // Правая сторона, движение влево.
            1 -> SpawnPoint(
                x = maxX,
                y = randomBetween(minY, maxY),
                directionRad = PI.toFloat() + randomSpread(),
            )

            // Верхняя сторона, движение вниз.
            2 -> SpawnPoint(
                x = randomBetween(minX, maxX),
                y = minY,
                directionRad = HALF_PI + randomSpread(),
            )

            // Нижняя сторона, движение вверх.
            else -> SpawnPoint(
                x = randomBetween(minX, maxX),
                y = maxY,
                directionRad = -HALF_PI + randomSpread(),
            )
        }
    }

    private fun randomBetween(from: Float, to: Float): Float =
        from + random.nextFloat() * (to - from)

    private fun randomSpread(): Float =
        (random.nextFloat() - 0.5f) * HALF_PI

    private fun publishState(
        phase: GamePhase,
        stats: RoundStats = state.stats,
    ) {
        val durationSec = currentSettings.roundDurationSec.toFloat()
        val remainingSec = (durationSec - elapsedSec).coerceAtLeast(0f)

        state = GameEngineState(
            phase = phase,
            bugs = bugs.toList(),
            stats = stats,
            remainingSec = remainingSec,
            elapsedSec = elapsedSec,
            settings = currentSettings,
        )
    }

    private data class SpawnPoint(
        val x: Float,
        val y: Float,
        val directionRad: Float,
    )

    private companion object {
        private val TWO_PI: Float = (2.0 * PI).toFloat()
        private val HALF_PI: Float = (PI / 2.0).toFloat()
    }
}
