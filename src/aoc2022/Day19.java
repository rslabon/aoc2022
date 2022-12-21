package aoc2022;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


record RobotCost(int ore, int cly, int obsidian) {
}

record Blueprint(int id, RobotCost ore, RobotCost cly, RobotCost obsidian, RobotCost geode) {
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

enum BuildRobot {
    NONE, ORE, CLY, OBSIDIAN, GEODE
}

public class Day19 {
    public static void main(String[] args) {
        String example1 = "Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.";
        String example2 = "Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.";

        Blueprint blueprint1 = parseBlueprint(example1);
        Blueprint blueprint2 = parseBlueprint(example2);
        List<Blueprint> examples = List.of(blueprint2);

        System.err.println(" part1=" + part1(examples));
    }

    private static int part1(List<Blueprint> blueprints) {
        Map<Blueprint, Integer> byBlueprint = new HashMap<>();
        for (Blueprint blueprint : blueprints) {
            System.err.println("Processing blueprint id=" + blueprint.id());
            cache.clear();
            maxSoFar.clear();
            TurnState turnState = new TurnState(new State(0, 0, 0, 0), new State(1, 0, 0, 0));
            int maxGeode = findMaxGeode(0, blueprint, turnState, BuildRobot.NONE);
            System.err.println(blueprint.id() + " max=" + maxGeode);
            byBlueprint.put(blueprint, maxGeode);
        }

        System.err.println(blueprints);
        int sum = 0;
        for (Map.Entry<Blueprint, Integer> e : byBlueprint.entrySet()) {
            sum += e.getValue();
        }
        return sum;
    }

    private static final Map<Integer, Integer> maxSoFar = new HashMap<>();
    private static final Map<List, Integer> cache = new HashMap<>();

    private static int findMaxGeode(int minute, Blueprint blueprint, TurnState turnState, BuildRobot build) {
        minute++;
        turnState = turnState.collect();
        if (build == BuildRobot.GEODE) {
            turnState = turnState.buildOGeodeRobot(blueprint);
        } else if (build == BuildRobot.OBSIDIAN) {
            turnState = turnState.buildObsidianRobot(blueprint);
        } else if (build == BuildRobot.CLY) {
            turnState = turnState.buildClyRobot(blueprint);
        } else if (build == BuildRobot.ORE) {
            turnState = turnState.buildOreRobot(blueprint);
        }

        if (cache.containsKey(List.of(minute, blueprint, turnState, build))) {
            return cache.get(List.of(minute, blueprint, turnState, build));
        }

        if (minute == 24) {
            System.err.println(turnState);
            cache.put(List.of(minute, blueprint, turnState, build), turnState.collected().geode());
            return turnState.collected().geode();
        }

        Integer maxTotal = maxSoFar.getOrDefault(minute, Integer.MIN_VALUE);
        if (
                turnState.collected().geode() < maxTotal
//                        (minute >= 22 && turnState.collected().geode() == 0) ||
//                        (minute >= 5 && turnState.robots().cly() == 0) ||
//                        (minute >= 18 && turnState.robots().obsidian() == 0)
////                        (turnState.canBuildGeode(blueprint) && prev.canBuildGeode(blueprint) && turnState.robots().geode() == prev.robots().geode()) ||
////                        (turnState.canBuildObsidian(blueprint) && prev.canBuildObsidian(blueprint) && turnState.robots().obsidian() == 0) ||
////                        (turnState.canBuildCly(blueprint) && prev.canBuildCly(blueprint) && turnState.robots().cly() == 0)
        ) {
//            cache.put(List.of(minute, blueprint, prev, turnState), Integer.MIN_VALUE);
            return turnState.collected().geode();
        }

        int max = -1;
        if (turnState.canBuildGeode(blueprint)) {
            max = Math.max(max, findMaxGeode(minute, blueprint, turnState, BuildRobot.GEODE));
        } else {
            int left = 24 - minute;
            if (turnState.canBuildObsidian(blueprint)) {
                max = Math.max(max, findMaxGeode(minute, blueprint, turnState, BuildRobot.OBSIDIAN));
            }
            if (turnState.canBuildCly(blueprint)) {
                max = Math.max(max, findMaxGeode(minute, blueprint, turnState, BuildRobot.CLY));
            }
            if (turnState.canBuildOre(blueprint)) {
                max = Math.max(max, findMaxGeode(minute, blueprint, turnState, BuildRobot.ORE));
            }
            max = Math.max(max, findMaxGeode(minute, blueprint, turnState, BuildRobot.NONE));
        }
//        cache.put(List.of(minute, blueprint, turnState, build), max);
        maxTotal = Math.max(max, maxTotal);
        maxSoFar.put(24 - minute, maxTotal);
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
