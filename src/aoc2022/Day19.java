package aoc2022;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


record RobotCost(int ore, int cly, int obsidian) {
}

record Blueprint(int id, RobotCost ore, RobotCost cly, RobotCost obsidian, RobotCost geode) {
    public RobotCost maxSpendPerMinute() {
        return new RobotCost(
                Stream.of(ore.ore(), cly().ore(), obsidian.ore(), geode.ore()).max(Integer::compareTo).get(),
                obsidian.cly(),
                geode().obsidian()
        );
    }
}

record State(int ore, int cly, int obsidian, int geode) {
}

record TurnState(State collected, State robots) {

    public TurnState collect() {
        return new TurnState(new State(
                collected.ore() + robots.ore(),
                collected.cly() + robots.cly(),
                collected.obsidian() + robots.obsidian(),
                collected.geode() + robots.geode()), robots);
    }

    public TurnState buildOreRobot(Blueprint blueprint) {
        if (!canBuildOre(blueprint)) {
            return this;
        }
        return new TurnState(new State(
                collected.ore() - blueprint.ore().ore(),
                collected.cly(),
                collected.obsidian(),
                collected.geode()),
                new State(robots.ore() + 1, robots.cly(), robots.obsidian(), robots.geode()));
    }

    public boolean canBuildOre(Blueprint blueprint) {
        return collected.ore() >= blueprint.ore().ore();
    }

    public TurnState buildClyRobot(Blueprint blueprint) {
        if (!canBuildCly(blueprint)) {
            return this;
        }
        return new TurnState(new State(
                collected.ore() - blueprint.cly().ore(),
                collected.cly(),
                collected.obsidian(),
                collected.geode()),
                new State(robots.ore(), robots.cly() + 1, robots.obsidian(), robots.geode()));
    }

    public boolean canBuildCly(Blueprint blueprint) {
        return collected.ore() >= blueprint.cly().ore();
    }

    public TurnState buildObsidianRobot(Blueprint blueprint) {
        if (!canBuildObsidian(blueprint)) {
            return this;
        }
        return new TurnState(new State(
                collected.ore() - blueprint.obsidian().ore(),
                collected.cly() - blueprint.obsidian().cly(),
                collected.obsidian(),
                collected.geode()),
                new State(robots.ore(), robots.cly(), robots.obsidian() + 1, robots.geode()));
    }

    public boolean canBuildObsidian(Blueprint blueprint) {
        return collected.ore() >= blueprint.obsidian().ore() && collected.cly() >= blueprint.obsidian().cly();
    }

    public TurnState buildOGeodeRobot(Blueprint blueprint) {
        if (!canBuildGeode(blueprint)) {
            return this;
        }
        return new TurnState(new State(
                collected.ore() - blueprint.geode().ore(),
                collected.cly(),
                collected.obsidian() - blueprint.geode().obsidian(),
                collected.geode()),
                new State(robots.ore(), robots.cly(), robots.obsidian(), robots.geode() + 1));
    }

    public boolean canBuildGeode(Blueprint blueprint) {
        return collected.ore() >= blueprint.geode().ore() && collected.obsidian() >= blueprint.geode().obsidian();
    }
}

enum Robot {
    NONE, ORE, CLY, OBSIDIAN, GEODE
}

public class Day19 {
    public static void main(String[] args) throws Exception {
        String example1 = "Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.";
        String example2 = "Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.";

        Blueprint blueprint1 = parseBlueprint(example1);
        Blueprint blueprint2 = parseBlueprint(example2);
//        List<Blueprint> blueprints = List.of(blueprint1, blueprint2);

        List<Blueprint> blueprints = Files.readAllLines(Path.of("resources/day19.txt"))
                .stream()
                .filter(s -> !s.isBlank())
                .map(Day19::parseBlueprint)
                .toList();

        System.err.println(" part1=" + part1(blueprints));
        System.err.println(" part2=" + part2(blueprints));
    }

    private static int part1(List<Blueprint> blueprints) {
        Map<Blueprint, Integer> byBlueprint = new HashMap<>();
        for (Blueprint blueprint : blueprints) {
            System.err.println("Processing blueprint id=" + blueprint.id());
            TurnState turnState = new TurnState(new State(0, 0, 0, 0), new State(1, 0, 0, 0));
            int geode = findMaxGeode(new HashMap<>(), 24, blueprint, turnState, Robot.NONE);
            byBlueprint.put(blueprint, geode);
            System.err.println(blueprint.id() + " = " + geode);
        }

        int sum = 0;
        for (Map.Entry<Blueprint, Integer> e : byBlueprint.entrySet()) {
            sum += e.getKey().id() * e.getValue();
        }
        return sum;
    }

    private static long part2(List<Blueprint> blueprints) {
        Map<Blueprint, Integer> byBlueprint = new HashMap<>();
        for (Blueprint blueprint : blueprints) {
            System.err.println("Processing blueprint id=" + blueprint.id());
            TurnState turnState = new TurnState(new State(0, 0, 0, 0), new State(1, 0, 0, 0));
            int geode = findMaxGeode(new HashMap<>(), 32, blueprint, turnState, Robot.NONE);
            byBlueprint.put(blueprint, geode);
            System.err.println(blueprint.id() + " = " + geode);
        }

        long mul = 1;
        for (Map.Entry<Blueprint, Integer> e : byBlueprint.entrySet()) {
            mul *= e.getValue();
        }
        return mul;
    }

    private static int maxSoFar = -10;


    private static int findMaxGeode(Map<List, Integer> cache, int minute, Blueprint blueprint, TurnState turnState, Robot build) {
        minute--;
        turnState = turnState.collect();
        if (build == Robot.GEODE) {
            turnState = turnState.buildOGeodeRobot(blueprint);
        } else if (build == Robot.OBSIDIAN) {
            turnState = turnState.buildObsidianRobot(blueprint);
        } else if (build == Robot.CLY) {
            turnState = turnState.buildClyRobot(blueprint);
        } else if (build == Robot.ORE) {
            turnState = turnState.buildOreRobot(blueprint);
        }

        List<Object> cacheKey = List.of(minute, blueprint, turnState);
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        if (minute == 0) {
            if (maxSoFar < turnState.collected().geode()) {
                System.err.println("new max = " + turnState.collected().geode());
            }
            maxSoFar = Math.max(maxSoFar, turnState.collected().geode());
            return turnState.collected().geode();
        }

        int max = -1;
        if (turnState.canBuildGeode(blueprint)) {
            max = Math.max(max, findMaxGeode(cache, minute, blueprint, turnState, Robot.GEODE));
        } else {
            if (blueprint.maxSpendPerMinute().obsidian() > turnState.robots().obsidian() && turnState.canBuildObsidian(blueprint)) {
                max = Math.max(max, findMaxGeode(cache, minute, blueprint, turnState, Robot.OBSIDIAN));
            }
            if (blueprint.maxSpendPerMinute().cly() > turnState.robots().cly() && turnState.canBuildCly(blueprint)) {
                max = Math.max(max, findMaxGeode(cache, minute, blueprint, turnState, Robot.CLY));
            }
            if (blueprint.maxSpendPerMinute().ore() > turnState.robots().ore() && turnState.canBuildOre(blueprint)) {
                max = Math.max(max, findMaxGeode(cache, minute, blueprint, turnState, Robot.ORE));
            }
            max = Math.max(max, findMaxGeode(cache, minute, blueprint, turnState, Robot.NONE));
        }
        cache.put(cacheKey, max);
        return max;
    }

    private static Blueprint parseBlueprint(String line) {
        Pattern p = Pattern.compile("Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.");
        Matcher m = p.matcher(line);
        m.find();
        Blueprint blueprint = new Blueprint(
                Integer.parseInt(m.group(1)),
                new RobotCost(Integer.parseInt(m.group(2)), 0, 0),
                new RobotCost(Integer.parseInt(m.group(3)), 0, 0),
                new RobotCost(Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)), 0),
                new RobotCost(Integer.parseInt(m.group(6)), 0, Integer.parseInt(m.group(7)))
        );
        return blueprint;
    }
}
