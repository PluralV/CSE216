package cse216hw3;
import java.util.*;
import java.util.function.Function;
import java.util.stream.*;


public class BijectionGroup {

    public static <T> Group<Function<T,T>> bijectionGroup(Set<T> domain){
        return new Group<Function<T,T>>() {

            private final List<Function<T,T>> bijections = bijectionsOf(domain);
            private final Function<T,T> identity = bijections.stream().findFirst().orElse(null);

            @Override
            //binary operations composes functions one, other in the form one(other(x))
            public Function<T, T> binaryOperation(Function<T, T> one, Function<T, T> other) {
                for (T t : domain){
                    if (!(domain.contains(one.apply(t))&&domain.contains(other.apply(t))))
                        throw new IllegalArgumentException("Function not in group passed.");
                }
                return new Function<T,T>() {
                    final List<T> FROM = new ArrayList<>(domain);
                    final List<T> TO = new ArrayList<>(domain).stream().map(n -> one.apply(other.apply(n))).collect(Collectors.toList());

                    public T apply(T t) {
                        int ind = FROM.indexOf(t);
                        if (ind!=-1) return TO.get(ind);
                        else throw new IllegalArgumentException("Argument not in domain.");
                    }
                };
            }

            @Override
            public Function<T, T> identity() {
                return identity;
            }

            @Override
            public Function<T, T> inverseOf(Function<T, T> ttFunction) {
                for (T t : domain)
                    if (!domain.contains(ttFunction.apply(t)))
                        throw new IllegalArgumentException("Argument function not in this group!");
                List<T> newdomain = new ArrayList<>(domain).stream().map(ttFunction).collect(Collectors.toList());
                List<T> domainList = new ArrayList<>(domain);
                ArrayList<Integer> domainIndexOrder = new ArrayList<>(domain.size());

                for (T t : domainList){
                    domainIndexOrder.add(newdomain.indexOf(t));
                }

                List<T> to_new_range = new ArrayList<>(domain.size());
                for (Integer i : domainIndexOrder){
                    to_new_range.add(domainList.get(i));
                }

                return new Function<T,T>() {
                    //straight up reverse domain -> range
                    //if f1(x) -> k, f1(x2) -> x, f1(k) -> x2
                    //returns function for which f2(k) -> x, f2(x2) -> k, f2(x) -> x2
                    //thus f2(f1(x)) -> f2(k) -> x and f1(f2(x)) -> f1(x2) -> x
                    //HOWEVER the domain/range must also be in order so that the exact function remains in the set
                    //right???
                    //probably
                    //To get the switched domain:
                    //1. ???
                    final List<T> FROM = domainList;

                    final List<T> TO = to_new_range;

                    public T apply(T t) {
                        int ind = FROM.indexOf(t);
                        if (ind!=-1) return TO.get(ind);
                        else throw new IllegalArgumentException("Argument not in domain.");
                    }
                };
            }
        };
    }

    public static <T> List<Function<T,T>> bijectionsOf(Set<T> domain){
        List<T> domainFrom = new ArrayList<>(domain);
        List<List<T>> perms = permutationsOf(domain);
        List<Function<T,T>> bijections = new ArrayList<>(perms.size());
        for (List<T> to : perms)
            bijections.add(new Function<T,T>(){
                final List<T> FROM = domainFrom;
                final List<T> TO = to;
                @Override
                public T apply(T t) {
                    int ind = FROM.indexOf(t);
                    if (ind!=-1) return TO.get(ind);
                    else throw new IllegalArgumentException("Argument not in domain.");
                }
            });

        return bijections;
    }

    private static <T> List<List<T>> permutationsOf (Set<T> domain){
        List<T> base = new ArrayList<>(domain);
        List<T> moddable = new ArrayList<>(base.size());
        moddable.addAll(base);
        return permutationsGoblinAssistant(
                moddable.size(),
                moddable,
                new ArrayList<>(factorial(1,moddable.size())));
    }

    private static int factorial(int acc, int i){
        if (i<=1) return acc;
        else return (factorial(acc*i,i-1));
    }

    private static <T> List<List<T>> permutationsGoblinAssistant (int size, List<T> permuted, List<List<T>> acc){
        if (size==1){
            acc.add(copyOf(permuted));
        }
        else {
            acc.addAll(permutationsGoblinAssistant(size-1,permuted,new LinkedList<>()));

            for (int i = 0; i<size-1;i++){
                if (size%2==0){
                    T ith = permuted.get(i);
                    permuted.set(i,permuted.get(size-1));
                    permuted.set(size-1,ith);
                }
                else {
                    T first = permuted.get(0);
                    permuted.set(0,permuted.get(size-1));
                    permuted.set(size-1,first);
                }
                acc.addAll(permutationsGoblinAssistant(size-1,permuted,new LinkedList<>()));
            }
        }
        return acc;
    }

    private static <T> List<T> copyOf(List<T> L){
        List<T> L2 = new ArrayList<>(L.size());
        L2.addAll(L);
        return L2;
    }


    public static void main(String... args) {
//        TreeSet<String> a = new TreeSet<>(Arrays.asList("ab", "g", "3", "KL"));
//        HashSet<List<String>> b = new HashSet<>(permutationsOf(a));
//        System.out.println(b.size() == permutationsOf(a).size()&&permutationsOf(a).size() == factorial(1,a.size()));
//        List<List<String>> ls = permutationsOf(a);
//        for (List<String> l : ls){
//            int count = 0;
//            for (List<String> l2 : ls){
//                if (l.equals(l2)){
//                    count++;
//                }
//            }
//            if (count>1) System.out.println("You suck lol");
//        }
//        List<Function<String,String>> bijectionsofa = bijectionsOf(a);
//        bijectionsofa.forEach(aBijection -> {
//            a.forEach(n -> System.out.print(n + " --> "+aBijection.apply(n)+"; "));
//            System.out.println();
//        });

        Set<Integer> a_few = Stream.of(1, 2, 3).collect(Collectors.toSet());
        // you have to figure out the data type in the line below
        List<Function<Integer,Integer>> bijections = bijectionsOf(a_few);
        bijections.forEach(aBijection -> {
            a_few.forEach(n -> System.out.printf("%d --> %d; ", n, aBijection.apply(n)));
            System.out.println();
        });

        Set<Integer> afew = Stream.of(1, 2, 3).collect(Collectors.toSet());
        // you have to figure out the data types in the lines below
        // some of these data types are functional objects, so look into java.util.function.Function
        Group<Function<Integer,Integer>> g = bijectionGroup(afew);
        Function<Integer,Integer> f1 = bijectionsOf(afew).stream().findFirst().orElse(null);
        Function<Integer,Integer> f2 = g.inverseOf(f1);
        Function<Integer,Integer> id = g.identity();
    }

}

