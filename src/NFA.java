import java.util.*;

public class NFA
{
    static final char EPSILON = 'ε';

    static class State
    {
        int id;
        Map<Character, Set<State>> transitions = new HashMap<>();

        State(int id)
        {
            this.id = id;
        }

        void addTransition(char symbol, State nextState)
        {
            transitions.computeIfAbsent(symbol, k -> new HashSet<>()).add(nextState);
        }
    }

    static int stateCount = 0;

    static State newState()
    {
        return new State(stateCount++);
    }

    static String addConcat(String regex)
    {
        StringBuilder result = new StringBuilder();
        int len = regex.length();
        for (int i = 0; i < len; i++)
        {
            char c = regex.charAt(i);
            result.append(c);
            if (i < len - 1)
            {
                char next = regex.charAt(i + 1);
                if ((Character.isLetterOrDigit(c) || c == '*' || c == ')') &&
                        (Character.isLetterOrDigit(next) || next == '('))
                {
                    result.append('.');
                }
            }
        }
        return result.toString();
    }

    static String toPostfix(String regex)
    {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        Map<Character, Integer> precedence = new HashMap<>();
        precedence.put('|', 1);
        precedence.put('.', 2);
        precedence.put('*', 3);

        for (char c : regex.toCharArray())
        {
            switch (c)
            {
                case '(':
                    stack.push(c);
                    break;
                case ')':
                    while (!stack.isEmpty() && stack.peek() != '(')
                    {
                        postfix.append(stack.pop());
                    }
                    if (!stack.isEmpty() && stack.peek() == '(')
                    {
                        stack.pop();
                    }
                    break;
                case '*':
                case '.':
                case '|':
                    while (!stack.isEmpty() && stack.peek() != '(' &&
                            precedence.get(stack.peek()) >= precedence.get(c))
                    {
                        postfix.append(stack.pop());
                    }
                    stack.push(c);
                    break;
                default:
                    postfix.append(c);
            }
        }
        while (!stack.isEmpty())
        {
            postfix.append(stack.pop());
        }
        return postfix.toString();
    }

    public static NFAFragment postfixToNFA(String postfix)
    {
        Stack<NFAFragment> stack = new Stack<>();
        for (char c : postfix.toCharArray())
        {
            if (c == '*')
            {
                NFAFragment frag = stack.pop();
                State start = newState();
                State accept = newState();
                start.addTransition(EPSILON, frag.start);
                start.addTransition(EPSILON, accept);
                frag.accept.addTransition(EPSILON, frag.start);
                frag.accept.addTransition(EPSILON, accept);
                stack.push(new NFAFragment(start, accept));
            }
            else if (c == '.')
            {
                NFAFragment frag2 = stack.pop();
                NFAFragment frag1 = stack.pop();
                frag1.accept.addTransition(EPSILON, frag2.start);
                stack.push(new NFAFragment(frag1.start, frag2.accept));
            }
            else if (c == '|')
            {
                NFAFragment frag2 = stack.pop();
                NFAFragment frag1 = stack.pop();
                State start = newState();
                State accept = newState();
                start.addTransition(EPSILON, frag1.start);
                start.addTransition(EPSILON, frag2.start);
                frag1.accept.addTransition(EPSILON, accept);
                frag2.accept.addTransition(EPSILON, accept);
                stack.push(new NFAFragment(start, accept));
            }
            else
            {
                State start = newState();
                State accept = newState();
                start.addTransition(c, accept);
                stack.push(new NFAFragment(start, accept));
            }
        }
        return stack.pop();
    }

    public static NFAFragment buildNFA(String regex)
    {
        stateCount = 0;
        String regexWithConcat = addConcat(regex);
        String postfix = toPostfix(regexWithConcat);
        return postfixToNFA(postfix);
    }

    public static void displayNFA(State start)
    {
        Set<Integer> visited = new HashSet<>();
        Queue<State> queue = new LinkedList<>();
        queue.add(start);

        System.out.println("Transition State Table:");
        System.out.println("State\tSymbol\tNext State(s)");
        while (!queue.isEmpty())
        {
            State s = queue.poll();
            if (visited.contains(s.id)) continue;
            visited.add(s.id);
            for (Map.Entry<Character, Set<State>> entry : s.transitions.entrySet())
            {
                char symbol = entry.getKey();
                for (State ns : entry.getValue())
                {
                    System.out.println(s.id + "\t" + symbol + "\t" + ns.id);
                    if (!visited.contains(ns.id))
                    {
                        queue.add(ns);
                    }
                }
            }
        }





    }

    // Display the NFA with start and goal states.
    public static void displayNFAFragment(NFAFragment nfa)
    {
        System.out.println("Start State: " + nfa.start.id);
        System.out.println("Goal (Accept) State: " + nfa.accept.id);
        displayNFA(nfa.start);
    }}