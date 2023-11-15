<script>
    import {Accordion, AccordionItem, Progress} from "sveltestrap";

    let data = [
        {id: 0, score: 1, summary: "lorem ipsum"},
        {id: 1, score: 20, summary: "lorem ipsum"},
        {id: 2, score: 15, summary: "lorem ipsum"},
        {id: 3, score: 1, summary: "lorem ipsum"},
        {id: 4, score: 60, summary: "lorem ipsum"},
        {id: 5, score: 80, summary: "lorem ipsum"},
        {id: 6, score: 90, summary: "lorem ipsum"},
        {id: 7, score: 5, summary: "lorem ipsum"},
        {id: 8, score: 17, summary: "lorem ipsum"},
        {id: 9, score: 19, summary: "lorem ipsum"},
        {id: 10, score: 23, summary: "lorem ipsum"},
    ]

    function calculateScoreBarColor(score) {
        switch (Number(score)) {

            case score > 75:
                return "success"
            case score > 25:
                return "warning"
            case score > 0:
                return "danger"
        }
    }


</script>

<Accordion>
    {#each data as entry}
        <AccordionItem>
            <div slot="header" class="accordion-header" style="width: 50%;" >
                <h4 >Entry id: {entry.id}</h4>
                <div style="">Score</div>
                <Progress value={entry.score} color="{calculateScoreBarColor(entry.score)}"/>
            </div>
            {entry.score}
            </AccordionItem>
    {/each}

</Accordion>

<style>
</style>