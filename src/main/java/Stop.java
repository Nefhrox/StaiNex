public record Stop(String id, String name) 
{
    public Stop 
    {
        // function must have actual id and name

        if (id == null || id.isBlank()) 
        {
            throw new IllegalArgumentException("Stop ID must not be blank");
        }

        if (name == null || name.isBlank()) 
        {
            throw new IllegalArgumentException("Stop name must not be blank");
        }
    }
}