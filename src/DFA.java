import java.util.*;

public class DFA
{
    static final char EPSILON = 'ε';

    public static class DFAState
    {
        int id;
        Set<NFA.State> nfaStates;
        Map<Character, DFAState> transitions = new HashMap<>();
        boolean isAccept;

        public DFAState(int id, Set<NFA.State> nfaStates, boolean isAccept)
        {
            this.id = id;
            this.nfaStates = nfaStates;
            this.isAccept = isAccept;
        }
    }

    static Set<NFA.State> epsilonClosure(Set<NFA.State> states)
    {
        Stack<NFA.State> stack = new Stack<>();
        Set<NFA.State> closure = new HashSet<>(states);
        for (NFA.State s : states)
        {
            stack.push(s);
        }
        while (!stack.isEmpty())
        {
            NFA.State s = stack.pop();
            Set<NFA.State> epsTransitions = s.transitions.get(EPSILON);
            if (epsTransitions != null)
            {
                for (NFA.State next : epsTransitions)
                {
                    if (!closure.contains(next))
                    {
                        closure.add(next);
                        stack.push(next);
                    }
                }
            }
        }
        return closure;
    }

    static Set<NFA.State> move(Set<NFA.State> states, char symbol)
    {
        Set<NFA.State> result = new HashSet<>();
        for (NFA.State s : states)
        {
            Set<NFA.State> trans = s.transitions.get(symbol);
            if (trans != null)
            {
                result.addAll(trans);
            }
        }
        return result;
    }

    static String stateSetKey(Set<NFA.State> states)
    {
        List<Integer> ids = new ArrayList<>();
        for (NFA.State s : states)
        {
            ids.add(s.id);
        }
        Collections.sort(ids);
        return ids.toString();
    }

    public static DFAState convertFromNFA(NFAFragment nfaFragment)
    {
        Set<Character> alphabet = new HashSet<>();
        Set<Integer> visitedNFA = new HashSet<>();
        Queue<NFA.State> nfaQueue = new LinkedList<>();
        nfaQueue.add(nfaFragment.start);
        while (!nfaQueue.isEmpty())
        {
            NFA.State s = nfaQueue.poll();
            if (visitedNFA.contains(s.id)) continue;
            visitedNFA.add(s.id);
            for (Map.Entry<Character, Set<NFA.State>> entry : s.transitions.entrySet())
            {
                char symbol = entry.getKey();
                if (symbol != EPSILON)
                {
                    alphabet.add(symbol);
                }
                for (NFA.State ns : entry.getValue())
                {
                    if (!visitedNFA.contains(ns.id))
                    {
                        nfaQueue.add(ns);
                    }
                }
            }
        }

        Map<String, DFAState> dfaStatesMap = new HashMap<>();
        Queue<DFAState> dfaQueue = new LinkedList<>();

        Set<NFA.State> startClosure = epsilonClosure(new HashSet<>(Arrays.asList(nfaFragment.start)));
        boolean isAccept = startClosure.contains(nfaFragment.accept);
        DFAState startDFA = new DFAState(0, startClosure, isAccept);
        dfaStatesMap.put(stateSetKey(startClosure), startDFA);
        dfaQueue.add(startDFA);
        int dfaIdCounter = 1;

        // Process each DFA state.
        while (!dfaQueue.isEmpty())
        {
            DFAState current = dfaQueue.poll();
            for (char symbol : alphabet)
            {
                Set<NFA.State> moveResult = move(current.nfaStates, symbol);
                if (moveResult.isEmpty()) continue;
                Set<NFA.State> closure = epsilonClosure(moveResult);
                String key = stateSetKey(closure);
                DFAState nextDFA = dfaStatesMap.get(key);
                if (nextDFA == null)
                {
                    boolean accept = closure.contains(nfaFragment.accept);
                    nextDFA = new DFAState(dfaIdCounter++, closure, accept);
                    dfaStatesMap.put(key, nextDFA);
                    dfaQueue.add(nextDFA);
                }
                current.transitions.put(symbol, nextDFA);
            }
        }
        return startDFA;
    }

    public static void displayDFA(DFAState start)
    {
        Set<Integer> visited = new HashSet<>();
        Queue<DFAState> queue = new LinkedList<>();
        queue.add(start);
        System.out.println("DFA Transition Table:");
        System.out.println("DFA State\tSymbol\tNext DFA State");
        List<Integer> acceptStates = new ArrayList<>();
        while (!queue.isEmpty())
        {
            DFAState s = queue.poll();
            if (visited.contains(s.id)) continue;
            visited.add(s.id);
            if (s.isAccept)
            {
                acceptStates.add(s.id);
            }
            for (Map.Entry<Character, DFAState> entry : s.transitions.entrySet())
            {
                System.out.println(s.id + "\t\t" + entry.getKey() + "\t\t" + entry.getValue().id);
                if (!visited.contains(entry.getValue().id)) {
                    queue.add(entry.getValue());
                }
            }
        }
        System.out.println("Accept States: " + acceptStates);
    }
}
