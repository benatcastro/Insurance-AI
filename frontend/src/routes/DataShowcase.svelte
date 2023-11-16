<script>
    import {Accordion, AccordionItem, Progress } from "sveltestrap";
    export let responseData;

    /**
     * Calculates the color of the progress bar taken the score
     * @param score
     */
    function calculateScoreBarColor(score) {
        let n = Number(score);

        if (n > 75)
            return "success";
        else if (n > 25) {
            return "warning"
        } else
            return "danger";
    }
</script>

<div class="container"></div>
    <Accordion>
        {#each responseData as entry}
            <AccordionItem active>
                <div slot="header" class="accordion-header" style="width: 50%;" >
                    <h4 >Entry id: {entry.id}</h4>
                    <div style="">Score</div>
                    <Progress value={entry.overall} color="{calculateScoreBarColor(entry.overall)}"></Progress>
                </div>
                <h6>Summary:</h6>
                {entry.summary}
            </AccordionItem>
        {/each}
    </Accordion>

<style>
</style>
