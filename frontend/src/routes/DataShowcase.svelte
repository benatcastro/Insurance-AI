<script>
    import {Accordion, AccordionItem, Progress, Spinner} from "sveltestrap";
    export let data;
    let dataMockUp = [
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
        data = "loading"
    ]

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
{#if (data == null)}
    <div>
        <h6>Waiting for request...</h6>

    </div>
{:else if (data === "loading")}
    <div class="status-tag">
        <p>Loading results</p>
        <Spinner></Spinner>
    </div>
{:else}
    <Accordion>
        {#each data as entry}
            <AccordionItem>
                <div slot="header" class="accordion-header" style="width: 50%;" >
                    <h4 >Entry id: {entry.id}</h4>
                    <div style="">Score</div>
                    <Progress value={entry.score} color="{calculateScoreBarColor(entry.score)}"/>
                </div>
                <h6>Summary:</h6>
                {entry.summary}
            </AccordionItem>
        {/each}
    </Accordion>
{/if}

<style>
    .status-tag {
        display: flex;
        flex-direction: column;
        align-items: center;

    }
</style>
