class NFAFragment
{
    NFA.State start;
    NFA.State accept;

    NFAFragment(NFA.State start, NFA.State accept)
    {
        this.start = start;
        this.accept = accept;
    }
}